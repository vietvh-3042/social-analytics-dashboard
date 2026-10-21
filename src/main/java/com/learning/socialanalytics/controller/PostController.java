package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import com.learning.socialanalytics.dto.ImportResultDto;
import com.learning.socialanalytics.dto.PostDto;
import com.learning.socialanalytics.entity.AdminUser;
import com.learning.socialanalytics.service.AdminUserService;
import com.learning.socialanalytics.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {
  private final AdminUserService adminUserService;
  private final PostService postService;

  @GetMapping(ApiPaths.POSTS)
  public List<PostDto> list(@AuthenticationPrincipal OAuth2User principal) {
    AdminUser owner = adminUserService.requireByFacebookSubject(principal.getName());
    return postService.list(owner);
  }

  @PostMapping(ApiPaths.POSTS + ApiPaths.POSTS_IMPORT)
  public ImportResultDto importPosts(
      @AuthenticationPrincipal OAuth2User principal, @RequestParam("file") MultipartFile file) {
    AdminUser owner = adminUserService.requireByFacebookSubject(principal.getName());
    return postService.importWorkbook(owner, file);
  }
}
