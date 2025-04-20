package com.app.CabBooking.controller;

import com.app.CabBooking.model.GeoLocation;
import com.app.CabBooking.service.GeoLocationProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired private GeoLocationProducer producer;

    @PostMapping("/send")
    public ResponseEntity<String> sendLocation(@RequestBody GeoLocation location) throws JsonProcessingException {
        producer.sendLocation(location);
        return ResponseEntity.ok("Location sent to Kafka successfully");
    }
}