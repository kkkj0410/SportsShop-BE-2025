package com.teamproject.back.repository;

import com.teamproject.back.entity.Item;
import com.teamproject.back.entity.ItemDetailImage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Slf4j
public class ItemDetailImageRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public ItemDetailImage save(Integer itemId, ItemDetailImage itemDetailImage){
        Item item = em.find(Item.class, itemId);

        itemDetailImage.fetchItem(item);
        em.persist(itemDetailImage);
        em.flush();

        return itemDetailImage;
    }

    @Transactional
    public List<ItemDetailImage> saveAll(Integer itemId, List<ItemDetailImage> itemDetailImages){

        Item item = em.find(Item.class, itemId);

//        String findItemJpql = "SELECT i " +
//                              "FROM Item i " +
//                              "JOIN FETCH i.itemDetailImages " +
//                              "WHERE i.id = :id";
//        Item item = em.createQuery(findItemJpql, Item.class)
//                .setParameter("id", itemId)
//                .getSingleResult();


        log.info("itemDetailImages : {}", itemDetailImages);

        itemDetailImages.forEach((itemDetailImage) -> {
            itemDetailImage.fetchItem(item);
            em.persist(itemDetailImage);
        });
        em.flush();

        return itemDetailImages;
    }


}
