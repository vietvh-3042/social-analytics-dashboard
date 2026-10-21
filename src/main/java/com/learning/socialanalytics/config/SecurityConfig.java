package com.learning.socialanalytics.config;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.constants.ApplicationConstants;
import com.learning.socialanalytics.service.FacebookOAuth2UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Value("${app.frontend-origin}")
  private String frontendOrigin;

  @Value("${app.cookie-secure:false}")
  private boolean secureCookies;

  @Value("${app.cookie-domain:}")
  private String cookieDomain;

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      FacebookOAuth2UserService facebookUserService,
      @Value("${app.frontend-url}") String frontendUrl)
      throws Exception {
    CookieCsrfTokenRepository csrfRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfRepository.setCookieName(ApplicationConstants.CSRF_COOKIE_NAME);
    csrfRepository.setCookiePath("/");
    csrfRepository.setCookieCustomizer(
        cookie -> {
          cookie.sameSite("Lax").secure(secureCookies);
          if (!cookieDomain.isBlank()) cookie.domain(cookieDomain);
        });

    http.cors(Customizer.withDefaults())
        .csrf(
            csrf ->
                csrf.csrfTokenRepository(csrfRepository)
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        ApiPaths.HEALTH,
                        ApiPaths.AUTH_CSRF,
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/oauth2/**",
                        "/login/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth ->
                oauth
                    .userInfoEndpoint(userInfo -> userInfo.userService(facebookUserService))
                    .defaultSuccessUrl(frontendUrl + "/dashboard", true)
                    .failureUrl(frontendUrl + "/login?error=facebook"))
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          response.setStatus(204);
                        }))
        .exceptionHandling(
            errors ->
                errors
                    .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED),
                        pathStartsWith(ApplicationConstants.API_BASE_PATH))
                    .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(org.springframework.http.HttpStatus.UNAUTHORIZED),
                        pathStartsWith(ApplicationConstants.WEBSOCKET_ENDPOINT)));
    return http.build();
  }

  private RequestMatcher pathStartsWith(String path) {
    return request -> request.getServletPath().startsWith(path);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(frontendOrigin));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN", "X-Requested-With"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    source.registerCorsConfiguration("/ws/**", configuration);
    return source;
  }
}
