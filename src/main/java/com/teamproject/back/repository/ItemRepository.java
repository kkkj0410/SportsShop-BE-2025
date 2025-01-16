package com.teamproject.back.repository;

import com.teamproject.back.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Item> findAllItem(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
