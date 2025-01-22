package com.teamproject.back.repository;

import com.teamproject.back.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Item> findAllItem(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Item save(Item item){
        entityManager.persist(item);
        entityManager.flush();
        return item;
    }

    @Transactional(readOnly = true)
    public Item findById(int id){
        String jpql = "SELECT i FROM Item i WHERE i.id = :id";

        try {
            return entityManager.createQuery(jpql, Item.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Item> findItemsWithPagination(int size, int page){
        String jpql = "SELECT i FROM Item i";

        try{
            return entityManager.createQuery(jpql, Item.class)
                    .setFirstResult(page)
                    .setMaxResults(size)
                    .getResultList();
        }catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public int deleteById(int id){
        String jpql = "DELETE FROM Item i WHERE i.id = :id";

        return entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
    }


    @Transactional
    public Item updateItem(Item item){
        String jpql = "UPDATE Item i SET " +
                "i.itemName = :itemName, " +
                "i.itemDesc = :itemDesc, " +
                "i.itemImg = :itemImg, " +
                "i.itemStock = :itemStock, " +
                "i.itemOriginPrice = :itemOriginPrice, " +
                "i.itemBrand = :itemBrand, " +
                "i.category = :category " +
                "WHERE i.id = :id";

        int count = entityManager.createQuery(jpql)
                .setParameter("itemName", item.getItemName())
                .setParameter("itemDesc", item.getItemDesc())
                .setParameter("itemImg", item.getItemImg())
                .setParameter("itemStock", item.getItemStock())
                .setParameter("itemOriginPrice", item.getItemOriginPrice())
                .setParameter("itemBrand", item.getItemBrand())
                .setParameter("category", item.getCategory())
                .setParameter("id", item.getId())
                .executeUpdate();

        if(count == 1){
            entityManager.flush();
            entityManager.clear();
            return item;
        }

        return null;
    }

    public List<Item> searchItemList(int page, int size, String itemName) {
        if (page < 1) {
            page = 1;
        }

        return em.createQuery("SELECT I FROM Item I WHERE I.itemName LIKE :itemName order by I.itemName desc", Item.class)
                .setParameter("itemName", "%" + itemName + "%")
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Item> findByAllItem(int page, int size) {
        if(page < 1){
            page = 1;
        }
        return em.createQuery("SELECT I FROM Item I")
                .setFirstResult((page-1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public int itemCount() {
        return em.createQuery("SELECT count(I) FROM Item I",Long.class).
                getSingleResult().intValue();
    }
}
