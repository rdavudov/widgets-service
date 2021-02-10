package com.miro.widgets.entity;

import java.time.LocalDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miro.widgets.dto.WidgetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Widget {
	
	@Id
	private String id ;
	
	@NotNull(message = "x coordinate can't be null")
	private Integer x ;
	
	@NotNull(message = "y coordinate can't be null")
	private Integer y ;
	
	@NotNull(message = "z index can't be null")
	private Integer zindex ;
	
	@NotNull(message = "height can't be null")
	@Min(value = 1, message = "height must be greater than 0")
	private Integer height ;
	
	@NotNull(message = "width can't be null")
	@Min(value = 1, message = "width must be greater than 0")
	private Integer width ;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@NotNull
	private LocalDateTime lastModificationDate ;
	
	@JsonIgnore
	public boolean isZindexNotSpecified() {
		return zindex == null ;
	}
	
	public static Widget buildFrom(WidgetDto dto) {
		return Widget.builder()
				.x(dto.getX())
				.y(dto.getY())
				.zindex(dto.getZindex())
				.height(dto.getHeight())
				.width(dto.getWidth())
				.lastModificationDate(LocalDateTime.now())
				.build() ;
	}
	
	public static Widget buildFrom(Widget widget) {
		return Widget.builder()
				.id(widget.getId())
				.x(widget.getX())
				.y(widget.getY())
				.zindex(widget.getZindex())
				.height(widget.getHeight())
				.width(widget.getWidth())
				.lastModificationDate(LocalDateTime.now())
				.build() ;
	}
}
