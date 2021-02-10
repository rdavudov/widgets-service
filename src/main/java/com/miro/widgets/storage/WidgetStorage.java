package com.miro.widgets.storage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.miro.widgets.entity.Widget;

public interface WidgetStorage {
	Optional<Widget> findById(String id) ;
	
	List<Widget> findAll(Pageable pageable) ;
	
	Widget create(Widget widget) ;
	
	Widget update(Widget widget) ;
	
	void deleteById(String id) ;
	
	void deleteAll() ;
}
