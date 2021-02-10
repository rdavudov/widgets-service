package com.miro.widgets.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.service.WidgetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/${api.version}/widgets")
@RequiredArgsConstructor
public class WidgetController {
	
	private final WidgetService widgetService ;
	
	@GetMapping("/{id}")
	public ResponseEntity<Widget> getWidget(@PathVariable("id") String id) {
		return widgetService.findById(id).map(w -> {
			return ResponseEntity.ok(w) ;
		}).orElse(ResponseEntity.notFound().build()) ;
	}
	
	@GetMapping
	public List<Widget> getWidgets(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		return widgetService.findAll(pageable) ;
	}
	
	@PostMapping
	public ResponseEntity<Widget> createWidget(@Valid @RequestBody WidgetDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(widgetService.create(dto)) ;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Widget> updateWidget(@PathVariable("id") String id, @Valid @RequestBody WidgetDto dto) {
		return widgetService.findById(id).map(w -> {
			return ResponseEntity.ok(widgetService.update(id, dto)) ;
		}).orElse(ResponseEntity.notFound().build()) ;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteWidget(@PathVariable("id") String id) {
		return widgetService.findById(id).map(w -> {
			widgetService.deleteById(id);
			return ResponseEntity.ok().build() ;
		}).orElse(ResponseEntity.notFound().build()) ;
	}
}
