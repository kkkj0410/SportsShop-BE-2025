package com.teamproject.back.repository;

import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.Likes;
import com.teamproject.back.entity.Users;
import com.teamproject.back.util.AesUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class LikeRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public Long findCountByCommentId(Long commentId){

        //JOIN FETCH + COUNT를 사용하면 에러남
        //JOIN 사용하면 N+1 문제 발생
        //급하게 JOIN FETCH + List Size 로 해결했으나, 다른 방법을 고민해봐야함
        String jpql = "SELECT l FROM Likes l " +
                      "JOIN FETCH l.comment c " +
                      "WHERE c.id = :commentId";

        return (long)em.createQuery(jpql)
                .setParameter("commentId", commentId)
                .getResultList().size();
    }

    @Transactional(readOnly = true)
    public boolean checkClicked(Long commentId, String email){
        String encryptedEmail = AesUtil.encrypt(email);

        String jpql = "SELECT l FROM Likes l " +
                "JOIN FETCH l.comment c " +
                "JOIN FETCH l.users u " +
                "WHERE c.id = :commentId " +
                "AND u.email = :email";


        try {
            em.createQuery(jpql, Likes.class)
                    .setParameter("commentId", commentId)
                    .setParameter("email", encryptedEmail)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }catch(NonUniqueResultException e){
            log.error("동일한 댓글에 대한 중복된 좋아요 값을 지니고 있음");
            throw new RuntimeException("동일한 댓글에 대한 중복된 좋아요 값입니다.", e);
        }

        return true;
    }

    @Transactional
    public Likes save(Long commentId, String email){
        String encryptedEmail = AesUtil.encrypt(email);

        // 중복된 좋아요 추가는 무시
        if(checkClicked(commentId, email)){
            return null;
        }

        Comment comment = em.find(Comment.class, commentId);

        String findUsersByEmail = "SELECT u FROM Users u " +
                                  "WHERE u.email = :email";
        try{
            Users user = em.createQuery(findUsersByEmail, Users.class)
                    .setParameter("email", encryptedEmail)
                    .getSingleResult();
            Likes like = Likes.builder().build();
            like.fetchComment(comment);
            like.fetchUser(user);

            em.persist(like);
            em.flush();
            return like;

        }catch(Exception e){
            log.error("회원 조회 실패");
            return null;
        }
    }

    @Transactional
    public int delete(Long commentId, String email){
        String encryptedEmail = AesUtil.encrypt(email);

        //l.comment, l.users를 불러오는 과정에서 +2번 쿼리 추가 소모
        //DELETE에 JOIN FETCH 사용 불가
        String jpql = "DELETE FROM Likes l " +
                "WHERE l.comment.id = :commentId " +
                "AND l.users.email = :email";

        return em.createQuery(jpql)
                .setParameter("commentId", commentId)
                .setParameter("email", encryptedEmail)
                .executeUpdate();
    }

    @Transactional
    public int deleteAllByCommentId(Long commentId){
        String jpql = "DELETE FROM Likes l " +
                    "WHERE l.comment.id = :commentId";

        return em.createQuery(jpql)
                .setParameter("commentId", commentId)
                .executeUpdate();
    }
}
