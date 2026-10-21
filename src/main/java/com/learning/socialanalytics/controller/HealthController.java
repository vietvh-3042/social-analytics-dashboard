package com.learning.socialanalytics.controller;

import com.learning.socialanalytics.constants.ApiPaths;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping(ApiPaths.HEALTH)
  public Map<String, String> health() {
    return Map.of("status", "ok");
  }
}
