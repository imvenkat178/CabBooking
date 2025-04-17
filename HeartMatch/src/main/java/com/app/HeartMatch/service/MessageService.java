package com.app.HeartMatch.service;

import com.app.HeartMatch.dto.ChatMessage;
import com.app.HeartMatch.model.Message;
import com.app.HeartMatch.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public Message saveMessage(ChatMessage msg) {
        Message m = new Message();
        m.setSenderId(msg.getFrom());
        m.setReceiverId(msg.getTo());
        m.setContent(msg.getMessage());
        m.setTimestamp(LocalDateTime.now());
        m.setStatus("SENT");
        return repo.save(m);
    }

    public List<Message> getMessagesBetweenUsers(Long user1, Long user2) {
        return repo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(user1, user2, user1, user2);
    }

    public void blockUser(Long blockerId, Long blockedId) {
        // Add custom logic if needed (store in a BlockList table)
    }
}