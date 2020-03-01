package project.ridersserver.ridersserverapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeRepository;
import project.ridersserver.ridersserverapp.dto.VideoViewLikeDto;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoViewLikeService {

    private VideoViewLikeRepository videoViewLikeRepository;

    @Transactional
    public VideoViewLikeEntity findByMemberNameAndVideoName(String memberName, String videoName){
        Optional<VideoViewLikeEntity> byMemberNameAndVideoName = videoViewLikeRepository.findByMemberNameAndVideoName(memberName, videoName);
        if(byMemberNameAndVideoName.isPresent())
            return byMemberNameAndVideoName.get();
        else{
            System.out.println("존재하지 않는 조회 관계!");
            return null;
        }
    }

    @Transactional
    public int updateSingleVideoLike(VideoViewLikeDto videoViewLikeDto, boolean isLike){
        return videoViewLikeRepository.updateSingleVideoLike(videoViewLikeDto.getMemberName(),videoViewLikeDto.getVideoName(),isLike);
    }

    @Transactional
    public Long saveVideoViewLike(VideoViewLikeDto videoLikeDto){
        if(videoViewLikeRepository.findByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName()).isPresent()){// 이미 있는경우
            System.out.println("이미 존재하는 조회 관계!");
            return new Long(-1);
        }else{
            return videoViewLikeRepository.save(videoLikeDto.toEntity()).getId();
        }
    }

    @Transactional
    public int deleteVideoViewLike(VideoViewLikeDto videoLikeDto){
        if(videoViewLikeRepository.findByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName()).isPresent() == false){// 없는경우
            System.out.println("삭제할 데이터가 없다!");
            return -1;
        }else{
            return videoViewLikeRepository.deleteByMemberNameAndVideoName(videoLikeDto.getMemberName(),videoLikeDto.getVideoName());
        }
    }

}
