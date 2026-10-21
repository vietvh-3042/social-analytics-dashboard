package com.learning.socialanalytics.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.learning.socialanalytics.controller.MetricController;
import com.learning.socialanalytics.service.FacebookOAuth2UserService;
import com.learning.socialanalytics.service.MetricSyncService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MetricController.class)
@Import(SecurityConfig.class)
@TestPropertySource(
    properties = {
      "app.frontend-origin=http://localhost:3000",
      "app.frontend-url=http://localhost:3000"
    })
class SpaCsrfTokenRequestHandlerTest {
  private static final String CSRF_TOKEN = "csrf-token-from-cookie";

  @Autowired private MockMvc mockMvc;

  @MockitoBean private MetricSyncService metricSyncService;
  @MockitoBean private FacebookOAuth2UserService facebookOAuth2UserService;
  @MockitoBean private ClientRegistrationRepository clientRegistrationRepository;

  @Test
  void acceptsRawCsrfTokenFromSpaCookieAndHeader() throws Exception {
    mockMvc
        .perform(
            post("/api/sync-now")
                .cookie(new Cookie("XSRF-TOKEN", CSRF_TOKEN))
                .header("X-XSRF-TOKEN", CSRF_TOKEN)
                .with(oauth2Login()))
        .andExpect(status().isOk());
  }

  @Test
  void rejectsUnsafeRequestWithoutCsrfToken() throws Exception {
    mockMvc.perform(post("/api/sync-now").with(oauth2Login())).andExpect(status().isForbidden());
  }
}
