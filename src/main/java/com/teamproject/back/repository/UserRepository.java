package com.teamproject.back.repository;


import com.teamproject.back.dto.oauth2.ProviderUser;
import com.teamproject.back.entity.Users;
import com.teamproject.back.util.AesUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public class UserRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Users save(Users users){
        entityManager.persist(users);
        entityManager.flush();

        return users;
    }



    @Transactional(readOnly = true)
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


    //Modifying(clearAutomatically = true)은 영속성 컨텍스트를 비움
    //Update는 DB에만 값을 반영함(영속성 컨텍스트에는 반영X)
    @Modifying(clearAutomatically = true)
    @Transactional
    public int patchUser(Users users){
        String jpql = "UPDATE Users u SET " +
                "u.username = :username, " +
                "u.birthday = :birthday " +
                "WHERE u.email = :email";

        int count = entityManager.createQuery(jpql)
                .setParameter("username", AesUtil.encrypt(users.getUsername()))
                .setParameter("birthday", users.getBirthday())
                .setParameter("email", AesUtil.encrypt(users.getEmail()))
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        return  count;
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    public int patchPassword(String email, String encodedPassword) {
        String encryptedEmail = AesUtil.encrypt(email);

        String jpql = "UPDATE Users u SET " +
                "u.password = :password " +
                "WHERE u.email = :email";

        int count = entityManager.createQuery(jpql)
                    .setParameter("password", encodedPassword)
                    .setParameter("email", encryptedEmail)
                    .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        return  count;

    }

    @Transactional(readOnly = true)
    public List<Users> findAllUsers(int page, int size) {
        return entityManager.createQuery("SELECT u from Users u")
                .setFirstResult((page-1)*size)
                .setMaxResults(size)
                .getResultList();
    }
    @Transactional(readOnly = true)
    public int userCount() {
       return entityManager.createQuery("SELECT COUNT(u) FROM Users u",Long.class)
               .getSingleResult().intValue();
    }

    public Users findById(Long id) {
        return entityManager.find(Users.class, id);
    }


}
