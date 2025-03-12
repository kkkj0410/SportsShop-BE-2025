package com.teamproject.back.repository;

import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.Users;
import com.teamproject.back.util.AesUtil;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CommentRepository {

    @PersistenceContext
    private EntityManager em;


    @Transactional(readOnly = true)
    public Comment findById(Long id){
        String jpql = "SELECT c FROM Comment c WHERE " +
                "c.id = :id";

        try {
            return em.createQuery(jpql, Comment.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> findComments(){

        String jpql = "SELECT c, c.parentComment.id " +
                      "FROM Comment c " +
                      "JOIN FETCH c.users u " +
                      "WHERE c.users.id = u.id";

//        String jpql = "SELECT c " +
//                        "FROM Comment c " +
//                        "JOIN FETCH c.parentComment cc " +
//                        "WHERE c.parentComment.id = cc.id";

        try{
            List<Object[]> results = em.createQuery(jpql, Object[].class)
                                    .getResultList();

            List<Comment> comments = new ArrayList<>();
            for(Object[] result : results){
                Comment comment = (Comment)result[0];
                Long parentCommentId = (Long) result[1];

                comment.setParentCommentId(parentCommentId);
                comments.add(comment);
            }
            return comments;
        }
        catch(NoResultException e){
            log.info("댓글 조회 실패");
            return null;
        }

    }

    @Transactional(readOnly = true)
    public List<Comment> findParentComments(){
        String jpql = "SELECT c.parentComment FROM Comment c";

        return em.createQuery(jpql, Comment.class)
                .getResultList();

    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByItemId(Integer itemId){
        String jpql = "SELECT c, p.id " +
                        "FROM Comment c " +
                        "JOIN FETCH c.users u " +
//                        "JOIN FETCH c.commentImages cc " +
                        "LEFT JOIN c.parentComment p " +
                        "WHERE c.item.id = :itemId ";


        try{
            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setParameter("itemId", itemId)
                    .getResultList();

            List<Comment> comments = new ArrayList<>();
            for(Object[] result : results){
                Comment comment = (Comment)result[0];
                Long parentCommentId = (Long) result[1];

                comment.setParentCommentId(parentCommentId);
                comments.add(comment);
            }
            return comments;
        }
        catch(NoResultException e){
            log.info("댓글 조회 실패");
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> findParentCommentsByItemId(Integer itemId, int size, int page){
        String jpql = "SELECT c FROM Comment c JOIN c.item i WHERE c.item.id = :itemId";
        //Item 객체 전체가 조회되는 문제 발생
        //문제점) user, item, parentComment 각 객체를 service 계층에서 조회하므로, +3번 쿼리 더 나감
        String jpql2 = "SELECT c FROM Comment c JOIN FETCH c.item i WHERE c.item.id = :itemId";

        String jpql6 = "SELECT c " +
                "FROM Comment c " +
                "LEFT JOIN FETCH c.parentComment p " +
                "JOIN FETCH c.item i " +
                "JOIN FETCH c.users u " +
                "WHERE p Is NULL " +
                "AND i.id = : itemId";


        return em.createQuery(jpql6, Comment.class)
                .setParameter("itemId", itemId)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Comment> findChildCommentsByIdWithUsers(Long id){
        //JOIN FETCH -> 해당 Comment의 users가 없어서 오류나는 것 같음
        String jpql2 = "SELECT DISTINCT c " +
                "FROM Comment c " +
                "JOIN c.users " +
                "JOIN FETCH c.parentComment " +
                "WHERE c.parentComment.id = :id";

        return em.createQuery(jpql2,Comment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public Comment save(Integer itemId, Comment comment){
        Item item = em.find(Item.class, itemId);
        if(item == null){
            return null;
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = em.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class)
                .setParameter("email", AesUtil.encrypt(email))
                .getSingleResult();

        comment.fetchUsers(user);
        comment.fetchItem(item);
        em.persist(comment);
        em.flush();
        return comment;
    }

    @Transactional
    public Comment saveReply(Long parentCommentId, String email,Comment childComment){
//        String jpql = "SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId";
//        String jpql2 = "SELECT c FROM Comment c FETCH JOIN c.item WHERE c.parentComment.id = :parentCommentId";
//        Comment parentComment = em.createQuery(jpql, Comment.class)
//                .setParameter("parentCommentId", parentCommentId)
//                .getSingleResult();
        String jpql = "SELECT u FROM Users u WHERE u.email = :email";
        Users users = em.createQuery(jpql, Users.class)
                .setParameter("email", AesUtil.encrypt(email))
                .getSingleResult();

        if(users == null){
            log.error("존재하지 않는 회원");
            return null;
        }

        Comment parentComment = em.find(Comment.class, parentCommentId);
        if(parentComment == null){
            log.error("존재하지 않는 부모 댓글");
            return null;
        }

        childComment.fetchUsers(users);
        childComment.fetchParentComment(parentComment);
        em.persist(childComment);
        em.flush();
        return childComment;
    }


    @Transactional
    public Comment update(Comment comment){
        String jpql = "UPDATE Comment c SET " +
                "c.content = :content WHERE " +
                "c.id = :id";

        int count = em.createQuery(jpql)
                .setParameter("content", comment.getContent())
                .setParameter("id", comment.getId())
                .executeUpdate();

        if(count == 1){
            em.flush();
            em.clear();
            return comment;
        }

        return null;
    }



    @Transactional
    public int delete(Long id){
        try{
            String deleteById = "DELETE FROM Comment c " +
                    "WHERE c.id = :id";
            return em.createQuery(deleteById)
                    .setParameter("id", id)
                    .executeUpdate();
        }catch(Exception e){
            log.error("댓글 삭제 실패");
            return 0;
        }
    }

    @Transactional
    public List<Comment> findChildCommentById(Long id){
        String findChildIdById = "SELECT c FROM Comment c " +
                            "JOIN FETCH c.parentComment cc " +
                            "WHERE cc.id = :id";

        try {

            return em.createQuery(findChildIdById, Comment.class)
                    .setParameter("id", id)
                    .getResultList();

        } catch (Exception e) {
            log.error("자식 댓글 조회 실패");
            return null;
        }
    }



    @Transactional(readOnly = true)
    public Double findAverageRating(Integer itemId){
        String findAllRatingJpql = "SELECT AVG(c.rating) FROM Comment c " +
                                   "JOIN c.item i " +
                                   "WHERE i.id = :itemId";

        try{
            Double averageRating = em.createQuery(findAllRatingJpql, Double.class)
                                    .setParameter("itemId", itemId)
                                    .getSingleResult();
            if(averageRating == null){
                log.info("평점 집계 실패");
                return null;
            }

            return averageRating;
        }catch(NoResultException e){
            log.error("평점이 존재하지 않습니다");
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Long> findIdsByItemId(Integer itemId){
        String jpql = "SELECT c.id FROM Comment c " +
                      "JOIN c.item i " +
                      "WHERE c.item.id = :itemId";


        return em.createQuery(jpql, Long.class)
                .setParameter("itemId", itemId)
                .getResultList();

    }



    @Transactional(readOnly = true)
    public Map<String, Object> findByRatingAndCommentCount(Long id) {
        return null;
    }
}
