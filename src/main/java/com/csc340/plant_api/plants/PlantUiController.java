package com.csc340.plant_api.plants;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class PlantUiController {
  private final PlantService service;

  public PlantUiController(PlantService service) {
    this.service = service;
  }

  // ----- Gallery with search -----
  @GetMapping({ "/", "/plants" })
  public String gallery(@RequestParam(required = false) String name,
      @RequestParam(required = false) LightNeeds light,
      Model model) {
    model.addAttribute("plants", service.search(name, light));
    model.addAttribute("name", name == null ? "" : name);
    model.addAttribute("selectedLight", light == null ? "" : light.name());
    return "index"; // templates/index.ftlh
  }

  // ----- Details -----
  @GetMapping("/plants/{id}")
  public String details(@PathVariable Long id, Model model) {
    try {
      Plant plant = service.findById(id);
      model.addAttribute("plant", plant);
      return "details"; // templates/details.ftlh
    } catch (RuntimeException exception) {
      model.addAttribute("errorMessage",
          exception.getMessage() == null ? "Plant not found" : exception.getMessage());
      return "error"; // templates/error.ftlh
    }
  }

  // ----- Add -----
  @GetMapping("/plants/new")
  public String newForm(Model model) {
    Plant plant = new Plant();
    plant.setLastWatered(LocalDate.now());
    plant.setWateringIntervalDays(7);
    model.addAttribute("plant", plant);
    model.addAttribute("formTitle", "Add a Plant");
    model.addAttribute("formAction", "/plants");
    return "plant-form";
  }

  @PostMapping("/plants")
  public String createPlant(@Valid @ModelAttribute Plant plant, MultipartFile imageFile) {
    Plant createdPlant = service.create(plant);
    if (imageFile != null && !imageFile.isEmpty()) {
      service.saveThumbnail(createdPlant, imageFile);
    }
    return "redirect:/plants/" + createdPlant.getId();
  }

  // ----- Edit -----
  @GetMapping("/plants/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    Plant plant = service.findById(id);
    model.addAttribute("plant", plant);
    model.addAttribute("formTitle", "Edit Plant");
    model.addAttribute("formAction", "/plants/" + id);
    return "plant-form";
  }

  @PostMapping("/plants/{id}")
  public String updatePlant(@PathVariable Long id, @Valid @ModelAttribute Plant plant, MultipartFile imageFile) {
    Plant updatedPlant = service.update(id, plant);
    if (imageFile != null && !imageFile.isEmpty()) {
      service.saveThumbnail(updatedPlant, imageFile);
    }
    return "redirect:/plants/" + updatedPlant.getId();
  }

  // ----- Delete -----
  @PostMapping("/plants/{id}/delete")
  public String deletePlant(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/";
  }

  @GetMapping("/about")
  public String about() {
    return "about";
  }
}
