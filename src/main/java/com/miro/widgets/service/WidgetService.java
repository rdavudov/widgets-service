package com.miro.widgets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;


public interface WidgetService {
	Optional<Widget> findById(String id) ;
	
	List<Widget> findAll(Pageable pageable) ;
	
	Widget create(WidgetDto dto) ;
	
	Widget update(String id, WidgetDto dto) ;
	
	void deleteById(String id) ;
}
