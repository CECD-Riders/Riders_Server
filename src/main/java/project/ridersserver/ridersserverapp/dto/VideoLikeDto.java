package project.ridersserver.ridersserverapp.dto;

import lombok.*;
import project.ridersserver.ridersserverapp.domain.Video.VideoLikeEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoLikeDto {
    private Long id;
    private String memberName;
    private String videoName;

    public VideoLikeEntity toEntity(){
        return VideoLikeEntity.builder()
                .id(id)
                .memberName(memberName)
                .videoName(videoName)
                .build();
    }

    @Builder
    public VideoLikeDto(Long id, String membername, String videoName){
        this.id = id;
        this.memberName = membername;
        this.videoName = videoName;
    }
}
