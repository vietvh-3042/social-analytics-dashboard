package com.learning.socialanalytics.repository;

import com.learning.socialanalytics.entity.SocialPost;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SocialPostRepository extends JpaRepository<SocialPost, UUID> {
  @Query(
      """
            select post
            from SocialPost post
            where post.owner.id = :ownerId
            order by post.importedAt desc
            """)
  List<SocialPost> findByOwner(@Param("ownerId") UUID ownerId);

  @Query(
      """
            select post
            from SocialPost post
            where post.owner.id = :ownerId
              and post.active = true
            """)
  List<SocialPost> findActiveByOwner(@Param("ownerId") UUID ownerId);

  @Query(
      """
            select case when count(post) > 0 then true else false end
            from SocialPost post
            where post.owner.id = :ownerId
              and post.postUrl = :postUrl
            """)
  boolean existsByOwnerAndUrl(@Param("ownerId") UUID ownerId, @Param("postUrl") String postUrl);
}
