package project.ridersserver.ridersserverapp.domain.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VideoViewLikeRepository extends JpaRepository<VideoViewLikeEntity,Long> {

    Optional<VideoViewLikeEntity> findByMemberNameAndVideoName(String memberName, String videoName);

    int deleteByMemberNameAndVideoName(String memberName,String videoName);

    @Modifying
    @Query(value = "UPDATE VideoViewLikeEntity v set v.isLike = :isLike where v.memberName = :memberName and v.videoName = :videoName")
    int updateSingleVideoLike(@Param("memberName")String memberName, @Param("videoName")String videoName, @Param("isLike") boolean isLike);
}
