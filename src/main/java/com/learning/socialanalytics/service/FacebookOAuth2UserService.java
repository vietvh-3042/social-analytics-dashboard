package com.learning.socialanalytics.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacebookOAuth2UserService extends DefaultOAuth2UserService {
  private final AdminUserService adminUserService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    OAuth2User user = super.loadUser(request);
    String subject = user.getName();
    if (subject == null || subject.isBlank()) {
      throw new OAuth2AuthenticationException("Facebook did not return a stable user ID");
    }
    adminUserService.upsertFacebookUser(
        subject,
        value(user, "name", "Facebook admin"),
        value(user, "email", null),
        pictureUrl(user.getAttributes().get("picture")));
    return user;
  }

  private String pictureUrl(Object picture) {
    if (!(picture instanceof Map<?, ?> pictureMap)) return null;
    Object data = pictureMap.get("data");
    if (!(data instanceof Map<?, ?> dataMap)) return null;
    Object url = dataMap.get("url");
    return url instanceof String string ? string : null;
  }

  private String value(OAuth2User user, String key, String fallback) {
    Object value = user.getAttributes().get(key);
    return value instanceof String string && !string.isBlank() ? string : fallback;
  }
}
