package com.learning.social_analytics_dashboard.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuth2Config {
    @Bean
    @ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${app.oauth2.facebook.client-id:}') or T(org.springframework.util.StringUtils).hasText('${app.oauth2.twitter.client-id:}')")
    ClientRegistrationRepository clientRegistrationRepository(Environment environment) {
        List<ClientRegistration> registrations = new ArrayList<>();
        addIfConfigured(registrations, environment, "facebook", "https://www.facebook.com/v19.0/dialog/oauth",
                "https://graph.facebook.com/oauth/access_token", "https://graph.facebook.com/me?fields=id,name");
        addIfConfigured(registrations, environment, "twitter", "https://twitter.com/i/oauth2/authorize",
                "https://api.twitter.com/2/oauth2/token", "https://api.twitter.com/2/users/me");
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private void addIfConfigured(List<ClientRegistration> registrations, Environment environment, String id,
                                 String authorizationUri, String tokenUri, String userInfoUri) {
        String clientId = environment.getProperty("app.oauth2." + id + ".client-id", "");
        if (!org.springframework.util.StringUtils.hasText(clientId)) return;
        ClientRegistration registration = ClientRegistration.withRegistrationId(id)
                .clientId(clientId)
                .clientSecret(environment.getProperty("app.oauth2." + id + ".client-secret", ""))
                .scope(environment.getProperty("app.oauth2." + id + ".scope", "openid,profile"))
                .authorizationUri(authorizationUri).tokenUri(tokenUri).userInfoUri(userInfoUri)
                .userNameAttributeName("id").redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        registrations.add(registration);
    }
}
