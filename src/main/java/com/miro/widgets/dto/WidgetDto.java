package com.miro.widgets.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WidgetDto {
	@NotNull(message = "x coordinate can't be null")
	private Integer x ;
	
	@NotNull(message = "y coordinate can't be null")
	private Integer y ;
	
	private Integer zindex ;
	
	@NotNull(message = "height can't be null")
	@Min(value = 1, message = "height must be greater than 0")
	private Integer height ;
	
	@NotNull(message = "width can't be null")
	@Min(value = 1, message = "width must be greater than 0")
	private Integer width ;
}
