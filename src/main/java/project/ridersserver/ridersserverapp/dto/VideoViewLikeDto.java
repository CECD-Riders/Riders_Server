package project.ridersserver.ridersserverapp.dto;

import lombok.*;
import project.ridersserver.ridersserverapp.domain.Video.VideoViewLikeEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoViewLikeDto {
    private Long id;
    private String memberName;
    private String videoName;
    private boolean isLike;

    public VideoViewLikeEntity toEntity(){
        return VideoViewLikeEntity.builder()
                .id(id)
                .memberName(memberName)
                .videoName(videoName)
                .isLike(isLike)
                .build();
    }

    @Builder
    public VideoViewLikeDto(Long id, String membername, String videoName, boolean isLike){
        this.id = id;
        this.memberName = membername;
        this.videoName = videoName;
        this.isLike = isLike;
    }
}
