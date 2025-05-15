package com.teamproject.back.repository;

import com.teamproject.back.entity.EmailToken;
import com.teamproject.back.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@Slf4j
public class EmailTokenRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public EmailToken save(EmailToken emailToken){
        em.persist(emailToken);
        em.flush();

        return emailToken;
    }

    @Transactional(readOnly = true)
    public EmailToken findById(Long id){
        String jpql = "SELECT e FROM EmailToken e " +
                "WHERE e.id = :id";

        try{
            return em.createQuery(jpql, EmailToken.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch(Exception e){
            log.error("emailToken 조회 실패");
            return null;
        }
    }

    @Transactional(readOnly = true)
    public EmailToken findByEmailAndAuthCode(String email, String authCode){
        String jpql = "SELECT e FROM EmailToken e " +
                "WHERE e.email = :email " +
                "AND e.authCode = :authCode";

        try{
            return em.createQuery(jpql, EmailToken.class)
                    .setParameter("email", email)
                    .setParameter("authCode", authCode)
                    .getSingleResult();
        }catch(Exception e){
            log.error("emailToken 조회 실패");
            return null;
        }
    }


    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void delete(){
        String jpql = "DELETE FROM EmailToken e " +
                "WHERE e.expiredTime < :currentTime";

        try{
            em.createQuery(jpql)
                    .setParameter("currentTime", LocalDateTime.now())
                    .executeUpdate();
        }catch(Exception e){
            log.error("만료 토큰 삭제 실패");
        }
    }

}
