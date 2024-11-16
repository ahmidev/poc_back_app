package com.poc_back_app.controller;

import com.poc_back_app.model.ChatMessage;
import com.poc_back_app.repository.ChatMessageRepository;
import com.poc_back_app.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository repository;
    private final ActiveUserService activeUserService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository repository, ActiveUserService activeUserService) {
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
        this.activeUserService = activeUserService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());

        boolean isReceiverActive = activeUserService.isUserActive(message.getReceiver());
        message.setRead(isReceiverActive);
        if (isReceiverActive) {
            System.out.println("Le destinataire " + message.getReceiver() + " est connecté. Message marqué comme lu.");
        } else {
            System.out.println("Le destinataire " + message.getReceiver() + " n'est pas connecté. Message marqué comme non lu.");
        }
        saveMessage(message);

        return message;
    }

    public void saveMessage(ChatMessage message) {
        try {
            repository.save(message);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToUser(String userId, ChatMessage message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/messages", message);
    }
}
