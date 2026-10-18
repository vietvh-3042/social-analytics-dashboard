package com.learning.social_analytics_dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                             ObjectProvider<ClientRegistrationRepository> registrations) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/dashboard", "/css/**", "/js/**", "/webjars/**",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health", "/api/chart-data", "/api/summary",
                        "/ws/**", "/login", "/api/csrf", "/oauth2/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                .anyRequest().authenticated())
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .formLogin(form -> form.defaultSuccessUrl("/dashboard", true).permitAll())
            .httpBasic(withDefaults())
            .logout(logout -> logout.logoutSuccessUrl("/dashboard").permitAll());
        if (registrations.getIfAvailable() != null) {
            http.oauth2Login(oauth -> oauth.defaultSuccessUrl("/dashboard", true));
        }
        return http.build();
    }

    @Bean
    UserDetailsService users(@Value("${app.admin.username}") String username,
                             @Value("${app.admin.password}") String password,
                             PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(User.withUsername(username)
                .password(encoder.encode(password)).roles("USER").build());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
