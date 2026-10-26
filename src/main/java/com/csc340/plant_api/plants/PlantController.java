package com.csc340.plant_api.plants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService service;

    public PlantController(PlantService service) {
        this.service = service;
    }

    @GetMapping
    public List<Plant> list(@RequestParam(required = false) String name,
            @RequestParam(required = false) LightNeeds light) {
        return service.search(name, light);
    }

    @GetMapping("/{id}")
    public Plant get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plant create(@Valid @RequestBody Plant plant) {
        return service.create(plant);
    }

    @PutMapping("/{id}")
    public Plant update(@PathVariable Long id, @Valid @RequestBody Plant plant) {
        return service.update(id, plant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /* Pulse check endpoint */
    // This endpoint can be used to verify that the API is running and responsive
    // without performing any actual plant-related operations.
    @GetMapping("/pulse")
    public ResponseEntity<String> checkPulse() {
        return ResponseEntity.ok("Pulse check successful");
    }
}
