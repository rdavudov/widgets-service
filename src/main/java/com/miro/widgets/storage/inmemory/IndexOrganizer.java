package com.miro.widgets.storage.inmemory;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.miro.widgets.entity.Widget;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IndexOrganizer {
	
	public int getMaxIndex(Map<String, Widget> widgetMap) {
		return widgetMap.values().stream().max(Comparator.comparingInt(Widget::getZindex)).map(w -> w.getZindex()).orElse(0) ;
	}
	
	public Map<String, Widget> shiftIndexes(Widget widget, Map<String, Widget> widgetMap) {
		Map<Integer, Widget> indexMap = createIndexMap(widgetMap) ;
		
		if (indexMap.containsKey(widget.getZindex())) {
			shift(widget.getZindex(), indexMap);
		}
		
		return createWidgetMap(indexMap) ;
	}
	
	private Map<Integer, Widget> createIndexMap(Map<String, Widget> widgetMap) {
		return widgetMap.values().stream().collect(Collectors.toMap(Widget::getZindex, Function.identity()));
	}
	
	private Map<String, Widget> createWidgetMap(Map<Integer, Widget> indexMap) {
		return indexMap.values().stream().collect(Collectors.toMap(Widget::getId, Function.identity()));
	}
	
	private void shift(int zindex, Map<Integer, Widget> indexMap) {
		Widget widget = indexMap.get(zindex) ;
		
		if (indexMap.containsKey(zindex + 1)) {
			shift(zindex + 1, indexMap);
		}
		
		indexMap.remove(zindex) ;
		log.info("shifting {} from {} to {}", widget.getId(), widget.getZindex(), widget.getZindex() + 1);
		Widget newWidget = Widget.buildFrom(widget) ;
		newWidget.setZindex(zindex + 1);
		
		indexMap.put(newWidget.getZindex(), newWidget) ;
	}
}
