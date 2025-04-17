package com.app.HeartMatch.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String location;

    @ElementCollection
    private List<String> placesVisited;

    @ElementCollection
    private List<String> foodPreferences;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getPlacesVisited() {
		return placesVisited;
	}

	public void setPlacesVisited(List<String> placesVisited) {
		this.placesVisited = placesVisited;
	}

	public List<String> getFoodPreferences() {
		return foodPreferences;
	}

	public void setFoodPreferences(List<String> foodPreferences) {
		this.foodPreferences = foodPreferences;
	}
    
    
}