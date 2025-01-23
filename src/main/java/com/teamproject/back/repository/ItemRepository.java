package com.teamproject.back.repository;

import com.teamproject.back.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.teamproject.back.entity.*;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ItemRepository {


    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(readOnly = true)
    public List<Item> findAllItem(){
        return entityManager.createQuery("select i from Item i", Item.class).getResultList();
    }

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

}
