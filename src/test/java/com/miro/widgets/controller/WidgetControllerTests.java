package com.miro.widgets.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.miro.widgets.utility.JacksonUtility.toJson;
import static com.miro.widgets.utility.JacksonUtility.fromJson;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.service.WidgetService;

@SpringBootTest
@AutoConfigureMockMvc
public class WidgetControllerTests {
	
	@Value("/api/${app.version:v1}/widgets")
	private String apiUri ;
	
	@Autowired
	private MockMvc mockMvc ;
	
	@MockBean
	private WidgetService widgetService ;
	
	@Test
	public void givenWidgetWhenRetrievedThenWidgetReturned() throws Exception {
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.findById(anyString())).thenReturn(Optional.of(widget)) ;
		
		MvcResult result = mockMvc.perform(get(apiUri + "/{id}", "abc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.x", is(0)))
				.andExpect(jsonPath("$.y", is(0)))
				.andExpect(jsonPath("$.zindex", is(1)))
				.andExpect(jsonPath("$.height", is(100)))
				.andExpect(jsonPath("$.width", is(50)))
				.andExpect(jsonPath("$.lastModificationDate", CoreMatchers.any(String.class)))
				.andExpect(jsonPath("$.id", CoreMatchers.any(String.class)))
				.andReturn();
	}
	
	@Test
	public void givenMissingWidgetWhenRetrievedThenNotFound() throws Exception {
		when(widgetService.findById(anyString())).thenReturn(Optional.empty()) ;
		
		MvcResult result = mockMvc.perform(get(apiUri + "/{id}", "abc"))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void givenWidgetsWhenRetrievedThenWidgetsAreSortedByzindexReturned() throws Exception {
		Widget widget1 = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		Widget widget2 = Widget.builder().id("def").x(0).y(0).zindex(2).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		Widget widget3 = Widget.builder().id("hij").x(0).y(0).zindex(3).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.findAll(any())).thenReturn(List.of(widget1, widget2, widget3)) ;
		
		MvcResult result = mockMvc.perform(get(apiUri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)))
				.andExpect(jsonPath("$[0].zindex", is(1)))
				.andExpect(jsonPath("$[1].zindex", is(2)))
				.andExpect(jsonPath("$[2].zindex", is(3)))
				.andReturn();
	}
	
	@Test
	public void givenEmptyWidgetsWhenRetrievedThenWidgetsAreSortedByzindexReturned() throws Exception {
		when(widgetService.findAll(any())).thenReturn(List.of()) ;
		
		MvcResult result = mockMvc.perform(get(apiUri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(0)))
				.andReturn();
	}
	
	@Test
	public void givenWidgetWhenSavedThenWidgetReturned() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build() ;
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.create(any(WidgetDto.class))).thenReturn(widget) ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.x", is(0)))
				.andExpect(jsonPath("$.y", is(0)))
				.andExpect(jsonPath("$.zindex", is(1)))
				.andExpect(jsonPath("$.height", is(100)))
				.andExpect(jsonPath("$.width", is(50)))
				.andExpect(jsonPath("$.lastModificationDate", CoreMatchers.any(String.class)))
				.andExpect(jsonPath("$.id", is("abc")))
				.andReturn();
	}
	
	@Test
	public void givenWidgetWhenUpdatedThenWidgetReturned() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(200).width(200).build() ;
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		Widget updated = Widget.builder().id("abc").x(0).y(0).zindex(1).height(200).width(200).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.findById(anyString())).thenReturn(Optional.of(widget)) ;
		when(widgetService.update(anyString(), any(WidgetDto.class))).thenReturn(updated) ;
		
		MvcResult result = mockMvc.perform(put(apiUri + "/{id}", "abc")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.x", is(0)))
				.andExpect(jsonPath("$.y", is(0)))
				.andExpect(jsonPath("$.zindex", is(1)))
				.andExpect(jsonPath("$.height", is(200)))
				.andExpect(jsonPath("$.width", is(200)))
				.andExpect(jsonPath("$.lastModificationDate", CoreMatchers.any(String.class)))
				.andExpect(jsonPath("$.id", is("abc")))
				.andReturn();
	}
	
	@Test
	public void givenMissingWidgetWhenUpdatedThenNotFound() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(200).width(200).build() ;
		
		when(widgetService.findById(anyString())).thenReturn(Optional.empty()) ;
		
		MvcResult result = mockMvc.perform(put(apiUri + "/{id}", "abc")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWhenDeletedThenSuccess() throws Exception {
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.findById(anyString())).thenReturn(Optional.of(widget)) ;
		doNothing().when(widgetService).deleteById(anyString());
		
		MvcResult result = mockMvc.perform(delete(apiUri + "/{id}", "abc"))
				.andExpect(status().isOk())
				.andReturn();
	}
	
	@Test
	public void givenMissingWidgetWhenDeletedThenNotFound() throws Exception {
		when(widgetService.findById(anyString())).thenReturn(Optional.empty()) ;
		
		MvcResult result = mockMvc.perform(delete(apiUri + "/{id}", "abc"))
				.andExpect(status().isNotFound())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithMissingXWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().y(0).zindex(1).height(100).width(50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithMissingYWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).zindex(1).height(100).width(50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithMissingzindexWhenSavedThenSuccess() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).height(100).width(50).build() ;
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetService.create(any(WidgetDto.class))).thenReturn(widget) ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.x", is(0)))
			.andExpect(jsonPath("$.y", is(0)))
			.andExpect(jsonPath("$.zindex", CoreMatchers.any(Integer.class)))
			.andExpect(jsonPath("$.height", is(100)))
			.andExpect(jsonPath("$.width", is(50)))
			.andExpect(jsonPath("$.lastModificationDate", CoreMatchers.any(String.class)))
			.andExpect(jsonPath("$.id", is("abc")))
			.andReturn();
	}
	
	@Test
	public void givenWidgetWithMissingHeightWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).width(50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithMissingWidthWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithZeroHeightXWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(0).width(50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithZeroWidthWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(0).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithNegativeHeightXWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(-100).width(50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	
	@Test
	public void givenWidgetWithNegativeWidthWhenSavedThenBadRequest() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(-50).build() ;
		
		MvcResult result = mockMvc.perform(post(apiUri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(dto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	

}
