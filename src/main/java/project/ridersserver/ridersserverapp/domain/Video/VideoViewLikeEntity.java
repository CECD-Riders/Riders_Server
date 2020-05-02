package project.ridersserver.ridersserverapp.domain.Video;

import lombok.*;

import javax.persistence.*;

@SequenceGenerator(name = "VIDEOLIKE_SEQ_GENERATOR", sequenceName = "VIDEOLIKE_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity
@Table(name = "videoviewlike")
public class VideoViewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VIDEOLIKE_SEQ_GENERATOR")
    private Long id;

    @Column(length = 100,nullable = false)
    private Long memberId;

    @Column(length = 100, nullable = false)
    private Long videoId;

    @Column(nullable = false)
    private boolean isLike;


}
