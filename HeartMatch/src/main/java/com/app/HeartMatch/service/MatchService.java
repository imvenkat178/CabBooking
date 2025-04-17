package com.app.HeartMatch.service;

import com.app.HeartMatch.model.*;
import com.app.HeartMatch.repository.*;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final UserRepository userRepository;

    public MatchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findMatches(Long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow();
        String userProfile = buildProfile(currentUser);
        RealVector vectorA = getSimpleEmbedding(userProfile);

        return userRepository.findAll().stream()
            .filter(u -> !u.getId().equals(userId))
            .sorted(Comparator.comparingDouble(u -> -cosineSimilarity(
                vectorA, getSimpleEmbedding(buildProfile(u))
            )))
            .limit(5)
            .collect(Collectors.toList());
    }

    private String buildProfile(User u) {
        StringBuilder profile = new StringBuilder();
        profile.append(u.getLocation()).append(" ");
        
        if (u.getPlacesVisited() != null) {
            profile.append(String.join(" ", u.getPlacesVisited())).append(" ");
        }
        
        if (u.getFoodPreferences() != null) {
            profile.append(String.join(" ", u.getFoodPreferences()));
        }
        
        return profile.toString();
    }

    // A simple embedding function that doesn't require external AI libraries
    private RealVector getSimpleEmbedding(String input) {
        // Normalize and clean the input
        String normalized = input.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        String[] words = normalized.split("\\s+");
        
        // Create a simple bag-of-words vectorization
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : words) {
            if (word.length() > 2) { // Skip very short words
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }
        
        // Use word hash codes to create a fixed-length vector
        double[] vector = new double[128]; // Using 128 dimensions
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            int hashCode = entry.getKey().hashCode();
            int index = Math.abs(hashCode) % vector.length;
            vector[index] += entry.getValue();
        }
        
        // Normalize the vector
        RealVector realVector = new ArrayRealVector(vector);
        if (realVector.getNorm() > 0) {
            realVector = realVector.mapDivide(realVector.getNorm());
        }
        
        return realVector;
    }

    private double cosineSimilarity(RealVector vectorA, RealVector vectorB) {
        if (vectorA.getNorm() == 0 || vectorB.getNorm() == 0) {
            return 0.0;
        }
        return vectorA.dotProduct(vectorB) / (vectorA.getNorm() * vectorB.getNorm());
    }
}