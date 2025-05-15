package com.teamproject.back.repository;

import com.teamproject.back.entity.Users;
import com.teamproject.back.util.AesUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Users findByEmail(String email) {
        String encryptedEmail = AesUtil.encrypt(email);
        String jpql = "SELECT u FROM Users u WHERE u.email = :email";

        try {
            return entityManager.createQuery(jpql, Users.class)
                    .setParameter("email", encryptedEmail)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
