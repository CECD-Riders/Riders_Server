package project.ridersserver.ridersserverapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.ridersserver.ridersserverapp.domain.Video.VideoLikeEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoLikeRepository;
import project.ridersserver.ridersserverapp.dto.VideoLikeDto;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoLikeService {

    private VideoLikeRepository videoLikeRepository;

    @Transactional
    public VideoLikeEntity findByMemberNameAndVideoName(String memberName, String videoName){
        Optional<VideoLikeEntity> byMemberNameAndVideoName = videoLikeRepository.findByMemberNameAndVideoName(memberName, videoName);
        if(byMemberNameAndVideoName.isPresent())
            return byMemberNameAndVideoName.get();
        else{
            System.out.println("존재하지 않는 추천 관계!");
            return null;
        }
    }

    @Transactional
    public Long saveVideoLike(VideoLikeDto videoLikeDto){
        if(videoLikeRepository.findByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName()).isPresent()){// 이미 있는경우
            System.out.println("이미 존재하는 관계!");
            return new Long(-1);
        }else{
            return videoLikeRepository.save(videoLikeDto.toEntity()).getId();
        }
    }

    @Transactional
    public int deleteVideoLike(VideoLikeDto videoLikeDto){
        if(videoLikeRepository.findByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName()).isPresent() == false){// 없는경우
            System.out.println("삭제할 데이터가 없다!");
            return -1;
        }else{
            return videoLikeRepository.deleteByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName());
        }
    }

}
