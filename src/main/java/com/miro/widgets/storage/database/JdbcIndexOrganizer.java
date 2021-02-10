package com.miro.widgets.storage.database;

import org.springframework.stereotype.Component;

import com.miro.widgets.entity.Widget;
import com.miro.widgets.repository.WidgetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JdbcIndexOrganizer {
	
	private final WidgetRepository widgetRepository ;
	
	public int getMaxIndex() {
		return widgetRepository.findFirstByOrderByZindexDesc().map(w -> w.getZindex()).orElse(0) ;
	}
	
	public void shiftIndexes(Widget widget) {
		int zindex = widget.getZindex() ;
		while (widgetRepository.findByZindex(zindex).isPresent()) {
			zindex++ ;
		}
		
		for (int i = zindex - 1; i >= widget.getZindex(); i--) {
			Widget existing = widgetRepository.findByZindex(i).get() ;
			log.info("shifting {} from {} to {}", existing.getId(), existing.getZindex(), existing.getZindex() + 1);
			existing.setZindex(existing.getZindex() + 1);
			widgetRepository.save(existing) ;
		}
	}
}
