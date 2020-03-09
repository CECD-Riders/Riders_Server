package project.ridersserver.ridersserverapp.domain.Video;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@SequenceGenerator(name = "VIDEO_SEQ_GENERATOR", sequenceName = "VIDEO_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "video")
public class VideoEntity {
	@Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="VIDEO_SEQ_GENERATOR")
    private Long id;
	
	@Column(name = "videoname", length = 100, nullable = false)
    private String name;
	
	@Column(name = "videolike",nullable = false)
    private Long like;

	@Column(name = "videoView",nullable = false)//조회수
	private Long view;

	@CreatedDate
	@Column(name = "createTimeAt",updatable = false)
	private LocalDateTime createTimeAt;

	@Column(name = "away", nullable = true, length = 4000)
	private String away;

	@Column(name = "home", nullable = true, length = 4000)
	private String home;

	@Column(name = "two", nullable = true, length = 4000)
	private String two;

	@Column(name = "three", nullable = true, length = 4000)
	private String three;

	@Column(name = "dunk", nullable = true, length = 4000)
	private String dunk;

	@Column(name = "block", nullable = true, length = 4000)
	private String block;






	@Builder
	public VideoEntity(Long id, String name, Long like, Long view, LocalDateTime createTimeAt,
					   String away, String home, String two, String three, String dunk, String block) {
		this.id = id;
		this.name = name;
		this.like = like;
		this.view = view;
		this.createTimeAt = createTimeAt;
		this.away = away;
		this.home = home;
		this.two = two;
		this.three = three;
		this.dunk = dunk;
		this.block = block;
	}
}






