package com.medis.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.csrf()
    // .ignoringRequestMatchers("/api/v1/course/enroll")
    // .disable()
    // .authorizeHttpRequests()
    // .requestMatchers("/api/v1/login", "/api/v1/register", "/api/v1/course",
    // "/api/v1/course/{slugName}")
    // .permitAll()
    // .and()
    // .authorizeHttpRequests().requestMatchers("/api/v1/course/**").authenticated()
    // .and()
    // .authorizeHttpRequests().requestMatchers("/api/v1/forum/**").authenticated()
    // .and()
    // .authorizeHttpRequests().requestMatchers("/api/v1/profile").authenticated()
    // .and()
    // .authorizeHttpRequests().requestMatchers("/api/v1/admin/**").hasAnyAuthority("client_coordinator");
    // http.oauth2ResourceServer()
    // .jwt()
    // .jwtAuthenticationConverter(jwtAuthConverter);
    // http.sessionManagement()
    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf()
                .ignoringRequestMatchers("/api/v1/course/enroll")
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/login", "/api/v1/register", "/api/v1/course",
                        "/api/v1/course/{slugName}")
                .permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/v1/course/**").hasAnyAuthority("DOKTER",
                        "ADMIN")
                .and()
                .authorizeHttpRequests().requestMatchers("/api/v1/forum/**").hasAnyAuthority("DOKTER",
                        "ADMIN")
                .and()
                .authorizeHttpRequests().requestMatchers("/api/v1/profile").hasAnyAuthority("DOKTER",
                        "ADMIN")
                .and()
                .authorizeHttpRequests().requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN")
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
