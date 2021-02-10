package com.miro.widgets.integration;

import static com.miro.widgets.utility.JacksonUtility.fromJson;
import static com.miro.widgets.utility.JacksonUtility.toJson;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.storage.inmemory.InMemoryWidgetStorage;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WidgetIntegrationTests {
	
	@Value("/api/${app.version:v1}/widgets")
	private String apiUri ;
	
	@Autowired
	private MockMvc mockMvc ;
	
	@Autowired
	private InMemoryWidgetStorage widgetStorage ;
	
	@BeforeEach
	public void setUp() {
		widgetStorage.deleteAll(); 
	}
	
	@Test
	public void givenWidgetWhenRetrievedThenWidgetReturned() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build() ;
		
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
				.andExpect(jsonPath("$.id", CoreMatchers.any(String.class)))
				.andReturn();
		
		Widget created = fromJson(result.getResponse().getContentAsString(), Widget.class) ;
		
		result = mockMvc.perform(get(apiUri + "/{id}", created.getId()))
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
	public void givenWidgetsWhenRetrievedThenWidgetsAreSortedByzindexReturned() throws Exception {
		List<WidgetDto> list = List.of(WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(2).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(3).height(100).width(50).build()) ;
		
		List<Widget> createdList = list.stream().map(dto -> {
			try {
				MvcResult result = mockMvc.perform(post(apiUri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(dto)))
					.andExpect(status().isCreated()).andReturn() ;
				
				Widget created = fromJson(result.getResponse().getContentAsString(), Widget.class) ;
				return created ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null ;
		}).collect(Collectors.toList());
		
		
		MvcResult result = mockMvc.perform(get(apiUri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)))
				.andExpect(jsonPath("$[0].zindex", is(1)))
				.andExpect(jsonPath("$[1].zindex", is(2)))
				.andExpect(jsonPath("$[2].zindex", is(3)))
				.andReturn();
	}
	

	
	@Test
	public void givenWidgetWhenUpdatedThenWidgetReturned() throws Exception {
		List<WidgetDto> list = List.of(WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(2).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(3).height(100).width(50).build()) ;
		
		List<Widget> createdList = list.stream().map(dto -> {
			try {
				MvcResult result = mockMvc.perform(post(apiUri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(dto)))
					.andExpect(status().isCreated()).andReturn() ;
				
				Widget created = fromJson(result.getResponse().getContentAsString(), Widget.class) ;
				return created ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null ;
		}).collect(Collectors.toList());
		
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(3).height(100).width(50).build() ;
		MvcResult result = mockMvc.perform(put(apiUri + "/{id}", createdList.get(1).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(dto)))
			.andExpect(status().isOk()).andReturn() ;
		
		result = mockMvc.perform(get(apiUri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)))
				.andExpect(jsonPath("$[0].zindex", is(1)))
				.andExpect(jsonPath("$[1].zindex", is(3)))
				.andExpect(jsonPath("$[2].zindex", is(4)))
				.andReturn();
	}
	
	@Test
	public void givenWidgetWhenPagedThenWidgetReturned() throws Exception {
		List<WidgetDto> list = List.of(WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(2).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(3).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(4).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(5).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(6).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(7).height(100).width(50).build(),
				WidgetDto.builder().x(0).y(0).zindex(8).height(100).width(50).build()
				) ;
		
		List<Widget> createdList = list.stream().map(dto -> {
			try {
				MvcResult result = mockMvc.perform(post(apiUri)
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(dto)))
					.andExpect(status().isCreated()).andReturn() ;
				
				Widget created = fromJson(result.getResponse().getContentAsString(), Widget.class) ;
				return created ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null ;
		}).collect(Collectors.toList());
		
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(3).height(100).width(50).build() ;
		MvcResult result = mockMvc.perform(put(apiUri + "/{id}", createdList.get(1).getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(dto)))
			.andExpect(status().isOk()).andReturn() ;
		
		result = mockMvc.perform(get(apiUri).param("size", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(5)))
				.andReturn();
		
		result = mockMvc.perform(get(apiUri).param("size", "5").param("page", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)))
				.andReturn();
	}
}
