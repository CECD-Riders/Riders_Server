package project.ridersserver.ridersserverapp.service;

import org.springframework.stereotype.Service;


import lombok.AllArgsConstructor;
import project.ridersserver.ridersserverapp.domain.Video.VideoEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoRepository;
import project.ridersserver.ridersserverapp.dto.VideoDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoService {
	private VideoRepository videoRepository;

	@Transactional
	public Long SaveSingleVideo(VideoDto videoDto) {

        if(videoRepository.findByName(videoDto.getName()).isPresent())
        	return new Long(-1);
        return videoRepository.save(videoDto.toEntity()).getId();
	}
	
	@Transactional
	public void DeleteSingleVideo(VideoDto videoDto) {
		System.out.println(videoDto); 
        System.out.println(videoDto.getName());
        if(videoRepository.findByName(videoDto.getName()).isPresent())
        	videoRepository.deleteByName(videoDto.getName());
	}
	
	@Transactional
	public int VideoOverlapCheck(String videoName) {
		Optional<VideoEntity> videoEntity = videoRepository.findByName(videoName);
		if(videoEntity.isPresent())
			return 1;
		else
			return -1;
	}

	@Transactional
	public List<String> GetLatestVideoName(){
		List<VideoEntity> allOrderByCreateTimeAt = videoRepository.findAllOrderByCreateTimeAt();
		List<String> fourLatestVideoName = new ArrayList<String>();
		for(int i = 0; i< 3;i++) {
			System.out.println(allOrderByCreateTimeAt.get(i).getName());
			fourLatestVideoName.add(allOrderByCreateTimeAt.get(i).getName());
		}

		return fourLatestVideoName;
	}

	@Transactional
	public List<String> GetMostLikeVideoName(){
		List<VideoEntity> allOrderByLike = videoRepository.findAllOrderByLike();
		List<String> fourMostLikeVideoName = new ArrayList<String>();
		for(int i = 0; i< 3;i++) {
			System.out.println(allOrderByLike.get(i).getName());
			fourMostLikeVideoName.add(allOrderByLike.get(i).getName());
		}
		return fourMostLikeVideoName;
	}
}
