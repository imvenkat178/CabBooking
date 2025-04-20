package com.app.CabBooking.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationConsumer {

    @KafkaListener(topics = {"user-location", "driver-location"}, groupId = "geo-group")
    public void listen(String message) {
        System.out.println("Received from Kafka: " + message);
    }
}