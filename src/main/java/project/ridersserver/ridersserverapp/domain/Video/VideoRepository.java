package project.ridersserver.ridersserverapp.domain.Video;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.ridersserver.ridersserverapp.domain.Video.VideoEntity;

public interface VideoRepository extends JpaRepository<VideoEntity, Long>{
	Optional<VideoEntity> findByName(String videoName);
	
	Optional<VideoEntity> deleteByName(String videoName);

	@Modifying
	@Query(value = "UPDATE VideoEntity v set v.like = :like where v.name = :videoName")
	int updateSingleVideoLike(@Param("like") Long like, @Param("videoName") String videoName);

	//최신순으로 4개 가져오기
	@Query(value = "SELECT v FROM VideoEntity v ORDER BY v.createTimeAt DESC")
	List<VideoEntity> findAllOrderByCreateTimeAt();

	//인기순으로 4개 가져오기
	@Query(value = "SELECT v FROM VideoEntity v ORDER BY v.like DESC")
	List<VideoEntity> findAllOrderByLike();



}
