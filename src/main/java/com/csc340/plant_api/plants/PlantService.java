package com.csc340.plant_api.plants;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PlantService {

    private final PlantRepository repository;

    private static final String UPLOAD_DIR = "src/main/resources/static/thumbnails/";

    public PlantService(PlantRepository repository) {
        this.repository = repository;
    }

    public List<Plant> findAll() {
        return repository.findAll();
    }

    public List<Plant> findByLightNeeds(LightNeeds lightNeeds) {
        return repository.findByLightNeeds(lightNeeds);
    }

    // Combines optional filters. Either argument may be null.
    public List<Plant> search(String name, LightNeeds lightNeeds) {
        boolean hasName = name != null && !name.isBlank();

        if (hasName && lightNeeds != null) {
            return repository.findByNameContainingIgnoreCaseAndLightNeeds(name.trim(), lightNeeds);
        }
        if (hasName) {
            return repository.findByNameContainingIgnoreCase(name.trim());
        }
        if (lightNeeds != null) {
            return repository.findByLightNeeds(lightNeeds);
        }
        return repository.findAll();
    }

    public Plant findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PlantNotFoundException(id));
    }

    public Plant create(Plant plant) {
        return repository.save(plant);
    }

    public Plant update(Long id, Plant updated) {
        Plant existing = findById(id);
        existing.setName(updated.getName());
        existing.setLightNeeds(updated.getLightNeeds());
        existing.setLastWatered(updated.getLastWatered());
        existing.setWateringIntervalDays(updated.getWateringIntervalDays());
        existing.setToxicToPets(updated.isToxicToPets());
        return repository.save(existing);
    }

    public void delete(Long id) {
        findById(id); // throws 404 if missing
        repository.deleteById(id);
    }

    public void saveThumbnail(Plant plant, MultipartFile file) {
        // Save the thumbnail file to a location and set the thumbnailUrl in the plant
        // For simplicity, let's assume we save it to a static directory and set the URL
        // accordingly
        String originalFileName = file.getOriginalFilename();
        try {
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            String newFileName = "plant_" + plant.getId() + "." + fileExtension;
            Path path = Paths.get(UPLOAD_DIR + newFileName);
            InputStream inputStream = file.getInputStream();
            Files.createDirectories(Paths.get(UPLOAD_DIR)); // Ensure the directory exists
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            plant.setThumbnailUrl( newFileName);
            repository.save(plant);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
