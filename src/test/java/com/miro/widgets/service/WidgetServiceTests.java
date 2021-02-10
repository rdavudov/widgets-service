package com.miro.widgets.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.service.WidgetService;
import com.miro.widgets.storage.WidgetStorage;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WidgetServiceTests {
	
	@Autowired
	private WidgetService widgetService ;
	
	@MockBean
	private WidgetStorage widgetStorage ;
	
	@Test
	public void givenWidgetWhenRetrievedThenWidgetReturned() throws Exception {
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		when(widgetStorage.findById(anyString())).thenReturn(Optional.of(widget)) ;
		Optional<Widget> wOptional = widgetService.findById("abc") ;
		
		assertThat(wOptional).isPresent() ;
	}
	
	@Test
	public void givenMissingWidgetWhenRetrievedThenNotFound() throws Exception {
		when(widgetStorage.findById(anyString())).thenReturn(Optional.empty()) ;
		
		Optional<Widget> wOptional = widgetService.findById("abc") ;
		
		assertThat(wOptional).isEmpty() ;
	}
	
	@Test
	public void givenWidgetsWhenRetrievedThenWidgetsAreSortedByZIndexReturned() throws Exception {
		Widget widget1 = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		Widget widget2 = Widget.builder().id("def").x(0).y(0).zindex(2).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		Widget widget3 = Widget.builder().id("hij").x(0).y(0).zindex(3).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetStorage.findAll(any())).thenReturn(List.of(widget1, widget2, widget3)) ;
		
		List<Widget> widgets = widgetService.findAll(any()) ;
		
		assertThat(widgets.size()).isEqualTo(3) ;
	}
	
	@Test
	public void givenEmptyWidgetsWhenRetrievedThenWidgetsAreSortedByZIndexReturned() throws Exception {
		when(widgetStorage.findAll(any())).thenReturn(List.of()) ;
		
		List<Widget> widgets = widgetService.findAll(any()) ;
		
		assertThat(widgets.size()).isEqualTo(0) ;
	}
	
	@Test
	public void givenWidgetWhenCreatedThenWidgetReturned() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(100).width(50).build() ;
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetStorage.create(any(Widget.class))).thenReturn(widget) ;
		
		Widget created = widgetService.create(dto) ;
		
		assertThat(created.getId()).isNotBlank() ;
		assertThat(created.getLastModificationDate()).isNotNull() ;
	}
	
	@Test
	public void givenWidgetWhenUpdatedThenWidgetReturned() throws Exception {
		WidgetDto dto = WidgetDto.builder().x(0).y(0).zindex(1).height(200).width(200).build() ;
		Widget widget = Widget.builder().id("abc").x(0).y(0).zindex(1).height(100).width(50).lastModificationDate(LocalDateTime.now()).build() ;
		
		when(widgetStorage.update(any(Widget.class))).thenReturn(widget) ;
		
		Widget updated = widgetService.update("abc", dto) ;
		
		assertThat(updated.getId()).isNotBlank() ;
		assertThat(updated.getLastModificationDate()).isNotNull() ;
	}

	
	@Test
	public void givenWidgetWhenDeletedThenSuccess() throws Exception {
		doNothing().when(widgetStorage).deleteById(anyString());
		
		widgetService.deleteById("abc");
	}
}
