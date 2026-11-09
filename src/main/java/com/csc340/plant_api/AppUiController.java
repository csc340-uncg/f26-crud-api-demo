package com.csc340.plant_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.csc340.plant_api.users.User;
import com.csc340.plant_api.users.UserService;

@Controller
public class AppUiController {
  private final UserService userService;

  public AppUiController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping({ "/" })
  public String homePage() {
    return "redirect:/plants";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @RequestMapping("/403")
  public String accessDenied() {
    return "403";
  }

  @GetMapping("/signup")
  public String signUp() {
    return "signup";
  }

  @PostMapping("/signup")
  public String signUp(User user) {
    userService.save(user);
    return "redirect:/login";
  }

  @GetMapping("/error")
  public String error() {
    return "error";
  }

}
