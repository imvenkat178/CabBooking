package com.app.HeartMatch.repository;

import com.app.HeartMatch.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long a, Long b, Long c, Long d);
}