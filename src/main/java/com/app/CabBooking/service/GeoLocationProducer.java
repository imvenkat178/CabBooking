package com.app.CabBooking.service;

import com.app.CabBooking.model.GeoLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationProducer {

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLocation(GeoLocation location) throws JsonProcessingException {
        String topic = location.getRole().equalsIgnoreCase("USER") ? "user-location" : "driver-location";
        String message = new ObjectMapper().writeValueAsString(location);
        kafkaTemplate.send(topic, location.getUserId(), message);
    }
}