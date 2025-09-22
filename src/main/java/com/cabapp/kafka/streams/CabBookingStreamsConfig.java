package com.cabapp.kafka.streams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class CabBookingStreamsConfig {

    @Bean
    public KStream<String, String> processDriverLocations(StreamsBuilder streamsBuilder) {
        KStream<String, String> driverLocationStream = streamsBuilder
                .stream("driver-locations", Consumed.with(Serdes.String(), Serdes.String()));

        // Real-time driver location processing
        driverLocationStream
                .peek((driverId, location) ->
                        log.debug("Processing location for driver {}: {}", driverId, location))
                .to("processed-driver-locations");

        return driverLocationStream;
    }

    @Bean
    public KStream<String, String> processRideEvents(StreamsBuilder streamsBuilder) {
        KStream<String, String> rideEventsStream = streamsBuilder
                .stream("ride-events", Consumed.with(Serdes.String(), Serdes.String()));

        // Real-time ride event processing
        rideEventsStream
                .filter((rideId, event) -> event.contains("REQUESTED"))
                .peek((rideId, event) ->
                        log.info("New ride request: {} - {}", rideId, event))
                .to("urgent-rides");

        return rideEventsStream;
    }

    @Bean
    public KTable<String, Long> driverLocationCounts(StreamsBuilder streamsBuilder) {
        // Count driver location updates in real-time
        return streamsBuilder
                .stream("driver-locations", Consumed.with(Serdes.String(), Serdes.String()))
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .groupBy((windowedKey, count) -> windowedKey.key())
                .reduce(Long::sum);
    }
}