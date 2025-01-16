package com.teamproject.back.repository;

import com.teamproject.back.entity.Chats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ChatRepository {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveChat(Chats chats) {
        em.persist(chats);
    }

    public List<String> findByAllChatList() {
      return em.createQuery("select u.email from Users u right join " +
                "u.chats c group by u.id,u.email",String.class).getResultList();
    }
}
