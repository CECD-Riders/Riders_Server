package project.ridersserver.ridersserverapp.domain.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VideoLikeRepository extends JpaRepository<VideoLikeEntity,Long> {

//    @Query(value = "SELECT v from VideoLikeEntity v WHERE v.memberName = ?1 and v.videoName = ?2")
    Optional<VideoLikeEntity> findByMemberNameAndVideoName(String memberName,String videoName);

//    @Query(value = "DELETE from VideoLikeEntity v WHERE v.memberName = ?1 and v.videoName = ?2")
    int deleteByMemberNameAndVideoName(String memberName,String videoName);
}
