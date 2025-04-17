package com.app.HeartMatch.controller;

import com.app.HeartMatch.dto.*;
import com.app.HeartMatch.model.*;
import com.app.HeartMatch.model.*;
import com.app.HeartMatch.repository.*;
import com.app.HeartMatch.service.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          MessageService messageService,
                          UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat")
    public void send(ChatMessage msg) {
        Message saved = messageService.saveMessage(msg);
        User recipient = userRepository.findById(msg.getTo()).orElseThrow();
        messagingTemplate.convertAndSendToUser(
            recipient.getEmail(), "/queue/messages", saved
        );
    }

    @GetMapping("/{user1Id}/{user2Id}")
    public List<Message> getConversation(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        return messageService.getMessagesBetweenUsers(user1Id, user2Id);
    }

    @PostMapping("/block")
    public void blockUser(@RequestParam Long blockerId, @RequestParam Long blockedId) {
        messageService.blockUser(blockerId, blockedId);
    }
}