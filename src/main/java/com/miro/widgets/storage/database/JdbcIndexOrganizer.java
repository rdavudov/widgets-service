package com.miro.widgets.storage.database;

import org.springframework.stereotype.Component;

import com.miro.widgets.entity.Widget;
import com.miro.widgets.repository.WidgetRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JdbcIndexOrganizer {
	
	private final WidgetRepository widgetRepository ;
	
	public int getMaxIndex() {
		return widgetRepository.findFirstByOrderByZindex().map(w -> w.getZindex()).orElse(0) ;
	}
	
	public void shiftIndexes(Widget widget) {
		int zindex = widget.getZindex() ;
		while (widgetRepository.findByZindex(zindex).isPresent()) {
			zindex++ ;
		}
		
		for (int i = zindex - 1; i >= widget.getZindex(); i--) {
			Widget existing = widgetRepository.findByZindex(i).get() ;
			existing.setZindex(existing.getZindex() + 1);
			widgetRepository.save(existing) ;
		}
	}
}
