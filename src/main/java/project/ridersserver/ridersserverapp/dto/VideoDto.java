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
	
	public VideoEntity toEntity() {
		return VideoEntity.builder()
				.id(id)
				.name(name)
				.like(like)
				.view(view)
				.createTimeAt(createTimeAt)
				.build();
	}
	
	@Builder
	public VideoDto(Long id, String name, Long like, Long view, LocalDateTime createTimeAt) {
		this.id = id;
		this.name = name;
		this.like = like;
		this.view = view;
		this.createTimeAt = createTimeAt;
	}
}
