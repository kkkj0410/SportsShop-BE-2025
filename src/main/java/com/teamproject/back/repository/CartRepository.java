package com.teamproject.back.repository;

import com.teamproject.back.entity.Cart;
import com.teamproject.back.entity.Comment;
import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.Users;
import com.teamproject.back.util.AesUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class CartRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Cart save(Cart cart, Integer itemId, String email) {
        if(findCart(itemId, email) != null){
            log.info("이미 장바구니에 있는 상품입니다.");
            return null;
        }

        Item item = em.find(Item.class, itemId);
        Users users = findUsers(email);

        if(item == null || users == null){
            return null;
        }

        cart.fetchItem(item);
        cart.fetchUsers(users);

        em.persist(cart);
        em.flush();

        return cart;

    }

    @Transactional(readOnly = true)
    public List<Cart> findCartAll(String email, int size, int page){
        String encryptedEmail = AesUtil.encrypt(email);

        String findCartByEmailJpql = "SELECT c " +
                                     "FROM Cart c " +
                                     "JOIN FETCH c.users u " +
                                     "JOIN FETCH c.item i " +
                                     "WHERE u.email = :email";

        return em.createQuery(findCartByEmailJpql, Cart.class)
                .setParameter("email", encryptedEmail)
                .setFirstResult((page-1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Cart findCart(Integer itemId, String email){
        String encryptedEmail = AesUtil.encrypt(email);
        String findCartJpql = "SELECT c " +
                              "FROM Cart c " +
                              "JOIN FETCH c.item i " +
                              "JOIN FETCH c.users u " +
                              "WHERE i.id = :itemId " +
                              "AND u.email = :email";

        try{
            return em.createQuery(findCartJpql, Cart.class)
                    .setParameter("itemId", itemId)
                    .setParameter("email", encryptedEmail)
                    .getSingleResult();
        }catch(NoResultException e){
            log.info("조회되지 않는 장바구니입니다.");
            return null;
        }
    }

    @Transactional
    public int updateCartQuantity(Cart cart, Integer itemId, String email) {
        String encryptedEmail = AesUtil.encrypt(email);
        String updateCartJpql = "UPDATE Cart c " +
                                "SET c.quantity = :quantity " +
                                "WHERE c.item.id = :itemId " +
                                "AND c.users.email = :email";

        return em.createQuery(updateCartJpql)
                .setParameter("quantity", cart.getQuantity())
                .setParameter("itemId", itemId)
                .setParameter("email", encryptedEmail)
                .executeUpdate();
    }

    @Transactional
    public int deleteCart(Long cartId) {
        String deleteCartJpql = "DELETE " +
                                "FROM Cart c " +
                                "WHERE c.id = :cartId";

        return em.createQuery(deleteCartJpql)
                .setParameter("cartId", cartId)
                .executeUpdate();
    }


    @Transactional(readOnly = true)
    public Users findUsers(String email){
        String encryptedEmail = AesUtil.encrypt(email);
        String findUsersByEmailJpql = "SELECT u FROM Users u " +
                                    "WHERE u.email = :email";

        try{
            return em.createQuery(findUsersByEmailJpql, Users.class)
                    .setParameter("email", encryptedEmail)
                    .getSingleResult();

        }catch(NoResultException e){
            log.info("회원이 없습니다");
            return null;
        }

    }

}
