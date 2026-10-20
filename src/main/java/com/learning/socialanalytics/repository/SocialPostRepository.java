package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.SocialPost;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialPostRepository extends JpaRepository<SocialPost, UUID> {
    List<SocialPost> findAllByOwner_IdOrderByImportedAtDesc(UUID ownerId);

    List<SocialPost> findAllByOwner_IdAndActiveTrue(UUID ownerId);

    boolean existsByOwner_IdAndPostUrl(UUID ownerId, String postUrl);
}
