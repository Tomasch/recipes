package com.kitchen.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private Environment environment;
    private final static String DEV_PROFILE_NAME = "dev";

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        if (isDevProfile()) {
            http.csrf().disable()
                    .authorizeRequests()
                    .anyRequest().permitAll();
        } else {
            http.formLogin(Customizer.withDefaults())
//                    .csrf().disable()
//                    .httpBasic();
//                    .and()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/index.html").permitAll()
                    .antMatchers("/swagger-ui/*").denyAll()
                    .anyRequest().authenticated();
        }
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin") //todo
//                .password("{bcrypt}$2a$10$b45aQw/AXCGXyzbckKjBQurbTsIUdGwmc.JwZLi/v9.l3xhx50v/a")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    private boolean isDevProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .anyMatch(DEV_PROFILE_NAME::equalsIgnoreCase);
    }
}
