package com.csc340.plant_api.security;
import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.csc340.plant_api.users.User;
import com.csc340.plant_api.users.UserRepository;

@Service
public class CustomPlantParentDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomPlantParentDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User appUser = userRepository.findByUsername(username);
    if (appUser == null) {
      throw new UsernameNotFoundException("User not found");
    }
    ArrayList<SimpleGrantedAuthority> authList = new ArrayList<>();
    String role = appUser.getRole();
    if (role != null) {
      if (role.equals("ADMIN")) {
        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authList.add(new SimpleGrantedAuthority("ROLE_PLANT_PARENT"));
        authList.add(new SimpleGrantedAuthority("ROLE_GUEST"));
      } else if (role.equals("PLANT_PARENT")) {
        authList.add(new SimpleGrantedAuthority("ROLE_PLANT_PARENT"));
        authList.add(new SimpleGrantedAuthority("ROLE_GUEST"));
      } else if (role.equals("GUEST")) {
        authList.add(new SimpleGrantedAuthority("ROLE_GUEST"));
      }
    }
    return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(),
        authList);
  }

}