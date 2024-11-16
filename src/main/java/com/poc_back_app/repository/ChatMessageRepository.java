package com.poc_back_app.repository;


import com.poc_back_app.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByReceiverAndIsReadFalse(String receiver);
}
