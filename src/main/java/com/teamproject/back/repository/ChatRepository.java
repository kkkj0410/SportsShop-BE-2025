package com.teamproject.back.repository;

import com.teamproject.back.entity.Chats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ChatRepository {
    @PersistenceContext
    private EntityManager em;


    public void saveChat(Chats chats) {
        em.persist(chats);
    }

    public List<String> findByAllChatList() {
      return em.createQuery("select u.email from Users u right join " +
                "u.chats c group by u.id,u.email",String.class).getResultList();
    }
    //최근 채팅 내역 10개로
    public List<Chats> findByUserChatList(String decodeUserName, int i) {
        return em.createQuery("select c from Chats c where c.roomName = :roomName order by c.sendAt DESC ",Chats.class)
                .setParameter("roomName",decodeUserName)
                .setMaxResults(i)
                .getResultList();
    }
}
