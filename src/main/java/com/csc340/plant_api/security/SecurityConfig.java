package com.csc340.plant_api.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;

import jakarta.servlet.DispatcherType;

import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomPlantParentDetailsService userDetailsService;

  public SecurityConfig(CustomPlantParentDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
    requestCache.setMatchingRequestParameterName(null);
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authorize) -> authorize
            .dispatcherTypeMatchers(DispatcherType.FORWARD,
                DispatcherType.ERROR)
            .permitAll()
            // api endpoints
            .requestMatchers(HttpMethod.GET, "/api/plants/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/plants").hasAnyAuthority("ROLE_ADMIN", "ROLE_PLANT_PARENT")
            .requestMatchers(HttpMethod.PUT, "/api/plants").hasAnyAuthority("ROLE_ADMIN", "ROLE_PLANT_PARENT")
            .requestMatchers(HttpMethod.DELETE, "/api/plants/**").hasAuthority("ROLE_ADMIN")
            // UI endpoints
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/static/**", "/thumbnails/**", "/style.css", "/**/*.css", "/*.jpg", "/*.png", "/*.gif").permitAll()
            .requestMatchers("/plants", "/", "/signup").permitAll()
            .requestMatchers("/plants/new", "/plants/*/edit").hasAnyAuthority("ROLE_ADMIN", "ROLE_PLANT_PARENT")
            .requestMatchers(HttpMethod.POST, "/plants/*/delete").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST, "/plants", "/plants/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PLANT_PARENT")
            .anyRequest().authenticated())
        .formLogin(form -> form
            .loginPage("/login").permitAll()
            .defaultSuccessUrl("/plants"))
        .exceptionHandling((x) -> x.accessDeniedPage("/403"))
        .logout(Customizer.withDefaults())
        .requestCache((cache) -> cache
            .requestCache(requestCache));

    return http.build();
  }

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
