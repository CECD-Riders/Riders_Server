package project.ridersserver.ridersserverapp.domain.Video;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@SequenceGenerator(name = "VIDEOLIKE_SEQ_GENERATOR", sequenceName = "VIDEOLIKE_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "videoViewLike")
public class VideoViewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VIDEOLIKE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "membername",length = 100,nullable = false)
    private String memberName;

    @Column(name = "videoname", length = 100, nullable = false)
    private String videoName;

    @Column(name = "islike", nullable = false)
    private boolean isLike;



    @Builder
    public VideoViewLikeEntity(Long id, String memberName, String videoName, boolean isLike){
        this.id = id;
        this.memberName = memberName;
        this.videoName = videoName;
        this.isLike = isLike;
    }

}
