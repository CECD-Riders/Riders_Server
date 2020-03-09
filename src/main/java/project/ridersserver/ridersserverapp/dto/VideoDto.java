package project.ridersserver.ridersserverapp.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import project.ridersserver.ridersserverapp.domain.Video.VideoEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoDto {
	private Long id;
	private String name;
	private Long like;
	private Long view;
	private LocalDateTime createTimeAt;
	private String away;
	private String home;
	private String two;
	private String three;
	private String dunk;
	private String block;
	
	public VideoEntity toEntity() {
		return VideoEntity.builder()
				.id(id)
				.name(name)
				.like(like)
				.view(view)
				.createTimeAt(createTimeAt)
				.away(away)
				.home(home)
				.two(two)
				.three(three)
				.dunk(dunk)
				.block(block)
				.build();
	}
	
	@Builder
	public VideoDto(Long id, String name, Long like, Long view, LocalDateTime createTimeAt,
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
