package com.app.CabBooking.repository;

import com.app.CabBooking.model.CabDriver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CabDriverRepository extends JpaRepository<CabDriver, String> {
    Optional<CabDriver> findByName(String name);
}