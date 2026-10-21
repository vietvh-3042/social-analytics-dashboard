package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.dto.AuthUserDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
  private final AdminUserService adminUserService;

  @GetMapping(ApiPaths.AUTH_CSRF)
  public CsrfResponse csrf(CsrfToken token) {
    return new CsrfResponse(token.getToken());
  }

  @GetMapping(ApiPaths.AUTH_ME)
  public AuthUserDto currentUser(@AuthenticationPrincipal OAuth2User principal) {
    AdminUser admin = adminUserService.requireByFacebookSubject(principal.getName());
    return new AuthUserDto(
        admin.getId(), admin.getDisplayName(), admin.getEmail(), admin.getPictureUrl());
  }

  public record CsrfResponse(String token) {}
}
