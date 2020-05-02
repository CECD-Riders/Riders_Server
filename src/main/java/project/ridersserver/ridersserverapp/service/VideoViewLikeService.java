package project.ridersserver.ridersserverapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeEntity;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoViewLikeService {

    private VideoViewLikeRepository videoViewLikeRepository;

    @Transactional
    public VideoViewLikeEntity findByMemberIdAndVideoId(Long memberId, Long videoId){
        Optional<VideoViewLikeEntity> byMemberIdAndVideoId = videoViewLikeRepository.findByMemberIdAndVideoId(memberId, videoId);
        if(byMemberIdAndVideoId.isPresent())
            return byMemberIdAndVideoId.get();
        else{
            System.out.println("존재하지 않는 조회 관계!");
            return null;
        }
    }

    @Transactional
    public int updateSingleVideoLike(VideoViewLikeEntity videoViewLikeEntity, boolean isLike){
        return videoViewLikeRepository.updateSingleVideoLike(videoViewLikeEntity.getMemberId(),videoViewLikeEntity.getVideoId(),isLike);
    }

    @Transactional
    public Long saveVideoViewLike(VideoViewLikeEntity videoViewLikeEntity){
        if(videoViewLikeRepository.findByMemberIdAndVideoId(videoViewLikeEntity.getMemberId(),videoViewLikeEntity.getVideoId()).isPresent()){// 이미 있는경우
            System.out.println("이미 존재하는 조회 관계!");
            return new Long(-1);
        }else{
            return videoViewLikeRepository.save(videoViewLikeEntity).getId();
        }
    }

    @Transactional
    public int deleteVideoViewLike(VideoViewLikeEntity videoViewLikeEntity){
        if(videoViewLikeRepository.findByMemberIdAndVideoId(videoViewLikeEntity.getMemberId(),videoViewLikeEntity.getVideoId()).isPresent() == false){// 없는경우
            System.out.println("삭제할 데이터가 없다!");
            return -1;
        }else{
            return videoViewLikeRepository.deleteByMemberIdAndVideoId(videoViewLikeEntity.getMemberId(),videoViewLikeEntity.getVideoId());
        }
    }

}
