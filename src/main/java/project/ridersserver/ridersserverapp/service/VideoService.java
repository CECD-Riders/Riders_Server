package project.ridersserver.ridersserverapp.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	public VideoEntity loadVideoByVideoname(String videoName){
		Optional<VideoEntity> videoEntityWrapper = videoRepository.findByName(videoName);
		if(!videoEntityWrapper.isPresent()) {
			System.out.println("비디오를 찾을수 없습니다!");
			return null;
		}
		else{
			return videoEntityWrapper.get();
		}
	}

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
	public int UpSingleVideoView(VideoDto videoDto){
		return videoRepository.updateSingleVideoView(videoDto.getView() + 1,videoDto.getName());
	}

	@Transactional
	public int UpSingleVideoLike(VideoDto videoDto){
		return videoRepository.updateSingleVideoLike(videoDto.getLike() + 1,videoDto.getName());
	}

	@Transactional
	public int DownSingleVideoLike(VideoDto videoDto){
		return videoRepository.updateSingleVideoLike(videoDto.getLike() - 1,videoDto.getName());
	}

	@Transactional
	public List<String> GetLatestVideoName(){
		List<VideoEntity> allOrderByCreateTimeAt = videoRepository.findAllOrderByCreateTimeAt();
		List<String> fourLatestVideoName = new ArrayList<String>();
		for(int i = 0; i< allOrderByCreateTimeAt.size();i++) {
			fourLatestVideoName.add(allOrderByCreateTimeAt.get(i).getName());
		}

		return fourLatestVideoName;
	}

	@Transactional
	public List<String> GetMostLikeVideoName(){
		List<VideoEntity> allOrderByLike = videoRepository.findAllOrderByLike();
		List<String> fourMostLikeVideoName = new ArrayList<String>();
		for(int i = 0; i< allOrderByLike.size();i++) {
			System.out.println(allOrderByLike.get(i).getName());
			fourMostLikeVideoName.add(allOrderByLike.get(i).getName());
		}
		return fourMostLikeVideoName;
	}
}
