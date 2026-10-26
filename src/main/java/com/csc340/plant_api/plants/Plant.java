package com.csc340.plant_api.plants;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LightNeeds lightNeeds;

    @NotNull
    private LocalDate lastWatered;

    @Positive
    private int wateringIntervalDays;

    private boolean toxicToPets;

    private String thumbnailUrl;

    public Plant() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LightNeeds getLightNeeds() {
        return lightNeeds;
    }

    public void setLightNeeds(LightNeeds lightNeeds) {
        this.lightNeeds = lightNeeds;
    }

    public LocalDate getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(LocalDate lastWatered) {
        this.lastWatered = lastWatered;
    }

    public int getWateringIntervalDays() {
        return wateringIntervalDays;
    }

    public void setWateringIntervalDays(int d) {
        this.wateringIntervalDays = d;
    }

    public boolean isToxicToPets() {
        return toxicToPets;
    }

    public void setToxicToPets(boolean toxicToPets) {
        this.toxicToPets = toxicToPets;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
