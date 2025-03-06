package com.teamproject.back.repository;

import com.teamproject.back.entity.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
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
//        String jpql = "SELECT i FROM Item i WHERE i.id = :id";

//        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
//                      "FROM Item i " +
//                      "LEFT JOIN Comment c " +
//                      "ON i.id = c.item.id " +
//                      "WHERE i.id = :id " +
//                      "GROUP BY i.id";

        String getItemJpql = "SELECT i " +
                      "FROM Item i " +
                      "JOIN FETCH i.itemDetailImages ii " +
                      "WHERE i.id = :id ";


        String getReviewJpql = "SELECT COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                                 "FROM Item i " +
                                 "LEFT JOIN Comment c " +
                                 "ON i.id = c.item.id " +
                                 "WHERE i.id = :id " +
                                 "GROUP BY i.id";


        try{

            Item findItem = entityManager.createQuery(getItemJpql, Item.class)
                    .setParameter("id", id)
                    .getSingleResult();

            Object[] result = entityManager.createQuery(getReviewJpql, Object[].class)
                                  .setParameter("id", id)
                                  .getSingleResult();

            findItem.setAverageRating((int) ((double)result[0]));
            findItem.setCommentCount((int) (long)result[1]);
            return findItem;

        }catch (NoResultException e) {
            log.info("상품 조회 실패");
            return null;
        }

    }

    //최신순
    @Transactional(readOnly = true)
    public List<Item> findItemsWithPagination(int size, int page){
//        String jpql = "SELECT i FROM Item i";
        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                "FROM Item i " +
                "LEFT JOIN Comment c ON i.id = c.item.id " +
                "GROUP BY i.id";

        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setFirstResult((page-1) * size)
                    .setMaxResults(size)
                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }

            if(items.isEmpty()){
                return null;
            }
            return items;
        }catch (NoResultException e) {
            return null;
        }
    }

    //가격 오름차순
    @Transactional(readOnly = true)
    public List<Item> findItemsSortedByPriceAsc(int size, int page){
//        String jpql = "SELECT i FROM Item i " +
//                "ORDER BY i.itemPrice ASC";


        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                      "FROM Item i " +
                      "LEFT JOIN Comment c ON i.id = c.item.id " +
                      "GROUP BY i.id " +
                      "ORDER BY i.itemPrice ASC";

        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setFirstResult((page-1) * size)
                    .setMaxResults(size)
                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }

            if(items.isEmpty()){
                return null;
            }
            return items;
        }catch (NoResultException e) {
            return null;
        }
    }

    //가격 내림차순
    @Transactional(readOnly = true)
    public List<Item> findItemsSortedByPriceDesc(int size, int page){
//        String jpql = "SELECT i FROM Item i " +
//                "ORDER BY i.itemPrice DESC";

        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                       "FROM Item i " +
                       "LEFT JOIN Comment c ON i.id = c.item.id " +
                       "GROUP BY i.id " +
                       "ORDER BY i.itemPrice DESC";

        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setFirstResult((page-1) * size)
                    .setMaxResults(size)
                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }

            if(items.isEmpty()){
                return null;
            }
            return items;
        }catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Item> findItemsSortedByRecommendDesc(int size, int page) {
        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                      "FROM Item i " +
                      "LEFT JOIN Comment c ON i.id = c.item.id " +
                      "GROUP BY i.id " +
                      "ORDER BY " +
                      "CASE WHEN AVG(c.rating) IS NULL THEN 1 ELSE 0 END, " +
                      "AVG(c.rating) DESC";


        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                                    .setFirstResult((page-1) * size)
                                    .setMaxResults(size)
                                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }

            if(items.isEmpty()){
                return null;
            }
            return items;
        }catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Item> findItemsSortedByComment(int size, int page){
        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                      "FROM Item i " +
                      "LEFT JOIN Comment c " +
                      "ON i.id = c.item.id " +
                      "GROUP BY i.id " +
                      "ORDER BY COUNT(c.id) DESC";

        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setFirstResult((page-1) * size)
                    .setMaxResults(size)
                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }

            if(items.isEmpty()){
                return null;
            }
            return items;
        }catch (NoResultException e) {
            return null;
        }
    }


    //추천순
//    @Transactional(readOnly = true)
//    public List<Item> findItemsSortedByRecommendDesc(int size, int page){
//
//        //"SELECT i, AVG(c.rating) "으로 해서 별도의 c.rating 작업없이 바로 평균 평점 가져오면 좋을듯
//        String recommendDescJqpl = "SELECT i " +
//                                   "FROM Item i " +
//                                   "JOIN Comment c " +
//                                   "ON i.id = c.item.id " +
//                                   "GROUP BY i.id " +
//                                   "ORDER BY AVG(c.rating) DESC";
//
//        String noRecommendJpql = "SELECT i " +
//                                 "FROM Item i " +
//                                 "LEFT JOIN Comment c " +
//                                 "ON i.id =  c.item.id " +
//                                 "WHERE c.item.id IS NULL";
//
//
//
//        List<Item> itemListByRecommendDesc = null;
//        List<Item> itemListByNoRecommend = null;
//        try {
//            itemListByRecommendDesc = entityManager.createQuery(recommendDescJqpl, Item.class)
//                    .setFirstResult((page - 1) * size)
//                    .setMaxResults(size)
//                    .getResultList();
//        }
//        catch (NoResultException e){
//            log.info("no recommend item");
//        }
//
//        try{
//            itemListByNoRecommend = entityManager.createQuery(noRecommendJpql, Item.class)
//                                   .setFirstResult((page-1) * size)
//                                   .setMaxResults(size)
//                                   .getResultList();
//        }catch (NoResultException e) {
//            log.info("no not recommend item");
//        }
//
//        List<Item> resultList = new ArrayList<>();
//        if (itemListByRecommendDesc != null) {
//            resultList.addAll(itemListByRecommendDesc);
//        }
//        if (itemListByNoRecommend != null) {
//            resultList.addAll(itemListByNoRecommend);
//        }
//
//        if(resultList.isEmpty()){
//            return null;
//        }
//
//        return resultList;
//    }



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

        return entityManager.createQuery("SELECT I FROM Item I WHERE I.itemName LIKE :itemName order by I.itemName desc", Item.class)
                .setParameter("itemName", "%" + itemName + "%")
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Item> findByAllItem(int page, int size) {
        if(page < 1){
            page = 1;
        }
        return entityManager.createQuery("SELECT I FROM Item I")
                .setFirstResult((page-1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public int itemCount() {
        return entityManager.createQuery("SELECT count(I) FROM Item I",Long.class).
                getSingleResult().intValue();
    }

    public List<Item> findByItemName(int page, int size,String itemName) {
        return entityManager.createQuery("SELECT I FROM Item  I where I.itemName =:itemName")
                .setParameter(itemName,itemName)
                .setFirstResult((page-1)*size)
                .setMaxResults(size)
                .getResultList();
    }

    public Item findByItemId(int id) {
        return entityManager.find(Item.class, id);
    }
    //메인화면에서 검색을 했을때
//    public List<Item> findByItemName(String debouncedSearch) {
//        return entityManager.createQuery("SELECT I FROM Item  I WHERE I.itemName LIKE :itemName ",Item.class)
//                .setParameter("itemName", "%"+debouncedSearch+"%")
//                .getResultList();
//    }

    //(3.3 - 상품 평점 + 리뷰 개수 조회 추가)
    public List<Item> findByItemName(String debouncedSearch) {
        String jpql = "SELECT i, COALESCE(AVG(c.rating), 0) AS avgRating, COALESCE(COUNT(c.id), 0) AS commentCount " +
                      "FROM Item i " +
                      "LEFT JOIN Comment c " +
                      "ON i.id = c.item.id " +
                      "WHERE i.itemName " +
                      "LIKE :itemName " +
                      "GROUP BY i.id";


        try{
            List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                    .setParameter("itemName", "%"+debouncedSearch+"%")
                    .getResultList();

            List<Item> items = new ArrayList<>();
            for(Object[] result : results){
                Item item = (Item) result[0];
                item.setAverageRating((int) ((double)result[1]));
                item.setCommentCount((int) (long)result[2]);
                items.add(item);
            }


            return items;
        }catch (Exception e) {
            log.info("상품 목록 조회 실패");
            return new ArrayList<>();
        }

    }
}
