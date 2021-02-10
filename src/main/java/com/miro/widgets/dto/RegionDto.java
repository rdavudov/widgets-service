package com.miro.widgets.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RegionDto {
	@NotNull(message = "x coordinate can't be null")
	private Integer x ;
	
	@NotNull(message = "y coordinate can't be null")
	private Integer y ;
	
	@NotNull(message = "height can't be null")
	@Min(value = 1, message = "height must be greater than 0")
	private Integer height ;
	
	@NotNull(message = "width can't be null")
	@Min(value = 1, message = "width must be greater than 0")
	private Integer width ;
	
	@JsonIgnore
	public int getArea() {
		return width * height ;
	}
}
