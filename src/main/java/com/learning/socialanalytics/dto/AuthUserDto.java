package com.learning.socialanalytics.dto;

import java.util.UUID;

public record AuthUserDto(UUID id, String name, String email, String pictureUrl) {}
