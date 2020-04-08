package project.ridersserver.ridersserverapp.domain.Video;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@SequenceGenerator(name = "VIDEO_SEQ_GENERATOR", sequenceName = "VIDEO_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Getter
@Setter
@Table(name = "video")
public class VideoEntity {
	@Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="VIDEO_SEQ_GENERATOR")
    private Long id;
	
	@Column(name = "videoname",length = 100, nullable = false , unique = true)
    private String name;
	
	@Column(name = "videolike",nullable = false)
    private Long like;

	@Column(name = "videoview", nullable = false)//조회수
	private Long view;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createTimeAt;

	@Column(nullable = true, length = 4000)
	private String away;

	@Column(nullable = true, length = 4000)
	private String home;

	@Column(nullable = true, length = 4000)
	private String two;

	@Column(nullable = true, length = 4000)
	private String three;

	@Column(nullable = true, length = 4000)
	private String dunk;

	@Column(nullable = true, length = 4000)
	private String block;


}






