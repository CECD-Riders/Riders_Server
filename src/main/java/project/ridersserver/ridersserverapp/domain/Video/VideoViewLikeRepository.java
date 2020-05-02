package project.ridersserver.ridersserverapp.domain.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoViewLikeRepository extends JpaRepository<VideoViewLikeEntity,Long> {

    Optional<VideoViewLikeEntity> findByMemberIdAndVideoId(Long memberId, Long videoId);

    int deleteByMemberIdAndVideoId(Long memberId, Long videoId);

    @Modifying
    @Query(value = "UPDATE VideoViewLikeEntity v set v.isLike = :isLike where v.memberId = :memberId and v.videoId = :videoId")
    int updateSingleVideoLike(@Param("memberId")Long memberId, @Param("videoId")Long videoId, @Param("isLike") boolean isLike);
}
