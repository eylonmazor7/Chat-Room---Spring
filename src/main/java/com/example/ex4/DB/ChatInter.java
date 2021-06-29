package com.example.ex4.DB;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Allows access to the database created at chatMessage.
 */
public interface ChatInter extends JpaRepository<ChatMessage, Long> {
    /**
     * find all the messages
     * @return all the messages
     */
    List<ChatMessage> findAll();

    /**
     * find last 5 messages received.
     * @return 5 last messages
     */
    List<ChatMessage> findTop5ByOrderByIdDesc();

    /**
     * find last message
     * @return last message
     */
    ChatMessage findTopByOrderByIdDesc();

    /**
     * find the user by username
     * @param userName - username to find
     * @return the user by username given
     */
    List<ChatMessage> findAllByUserNameOrderByIdDesc(String userName);
}
