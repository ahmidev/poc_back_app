package com.poc_back_app.controller;

import com.poc_back_app.model.ChatMessage;
import com.poc_back_app.repository.ChatMessageRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ChatRestController {

    private final ChatMessageRepository repository;

    public ChatRestController(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/messages/unread")
    public List<ChatMessage> getUnreadMessages(@RequestParam String receiver) {
        return repository.findByReceiverAndIsReadFalse(receiver);
    }

    @PostMapping("/messages/mark-as-read")
    public void markMessagesAsRead(@RequestParam String receiver) {
        List<ChatMessage> unreadMessages = repository.findByReceiverAndIsReadFalse(receiver);
        unreadMessages.forEach(message -> message.setRead(true));
        repository.saveAll(unreadMessages);
    }
}

