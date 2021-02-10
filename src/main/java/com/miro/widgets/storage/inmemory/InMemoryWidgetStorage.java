package com.miro.widgets.storage.inmemory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.miro.widgets.dto.RegionDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.storage.WidgetStorage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "storage", havingValue = "inmemory", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class InMemoryWidgetStorage implements WidgetStorage {
	
	private final IndexOrganizer zIndexOrganizer ;
	private CombinedStorage storage = new CombinedStorage(new HashMap<>(), new ArrayList<>()) ;
	private ReentrantLock lock = new ReentrantLock() ;

	@Override
	public Optional<Widget> findById(String id) {
		return Optional.ofNullable(storage.getWidgetMap().get(id));
	}

	@Override
	public List<Widget> findAll(Pageable pageable) {
		return storage.getWidgetMap().values().stream()
				.sorted(Comparator.comparingInt(Widget::getZindex))
				.skip(pageable.getPageNumber() * pageable.getPageSize())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
	}

	@Override
	public Widget create(Widget widget) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(storage.getWidgetMap());
			
			if (widget.isZindexNotSpecified()) {
				widget.setZindex(zIndexOrganizer.getMaxIndex(updatedMap) + 1);
				log.info("widget {} has no z index using max value {}", widget.getId(), widget.getZindex());
			} else {
				log.info("shifting other widgets");
				updatedMap = zIndexOrganizer.shiftIndexes(widget, updatedMap) ;
			}

			updatedMap.put(widget.getId(), widget) ;
			storage = new CombinedStorage(updatedMap, sortByArea(updatedMap)) ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public Widget update(Widget widget) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(storage.getWidgetMap());
			
			if (zIndexHasBeenModified(widget, updatedMap)) {
				log.info("widget has modified z index");
				log.info("shifting other widgets");
				updatedMap.remove(widget.getId()) ;
				updatedMap = zIndexOrganizer.shiftIndexes(widget, updatedMap) ;
			}

			updatedMap.put(widget.getId(), widget) ;
			storage = new CombinedStorage(updatedMap, sortByArea(updatedMap)) ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public void deleteById(String id) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(storage.getWidgetMap());
			updatedMap.remove(id) ;
			storage = new CombinedStorage(updatedMap, sortByArea(updatedMap)) ;
		} finally {
			lock.unlock(); 
		}
	}

	@Override
	public void deleteAll() {
		lock.lock(); 
		try {
			storage = new CombinedStorage(new HashMap<>(), new ArrayList<>()) ;
		} finally {
			lock.unlock(); 
		}
	}
	
	@Override
	public List<Widget> findAllByRegion(RegionDto region) {
		List<Widget> withInRegion = new ArrayList<>() ;
		for (Widget widget : storage.getWidgetsSortedByArea()) {
			if (widget.getArea() <= region.getArea()) {
				withInRegion.add(widget) ;
			} else {
				break ;
			}
		}
		
		return withInRegion
				.stream()
				.filter(w -> w.getX() >= region.getX() && w.getX() <= region.getX() + region.getWidth()
						  && w.getX() + w.getWidth() >= region.getX() &&  w.getX() + w.getWidth() <= region.getX() + region.getWidth()
						  && w.getY() >= region.getY() && w.getY() <= region.getY() + region.getHeight()
						  && w.getY() + w.getHeight() >= region.getY() && w.getY() + w.getHeight() <= region.getY() + region.getHeight())
				.collect(Collectors.toList());
	}

	private boolean zIndexHasBeenModified(Widget widget, Map<String, Widget> updatedMap) {
		return !updatedMap.get(widget.getId()).getZindex().equals(widget.getZindex()) ;
	}

	private List<Widget> sortByArea(Map<String, Widget> widgetMap) {
		return widgetMap.values().stream().sorted(Comparator.comparingInt(Widget::getArea)).collect(Collectors.toList()) ;
	}
	
	@Getter
	private class CombinedStorage {
		private Map<String, Widget> widgetMap = new HashMap<>() ;
		private List<Widget> widgetsSortedByArea ;
		
		public CombinedStorage(Map<String, Widget> widgetMap, List<Widget> widgetsSortedByArea) {
			this.widgetMap = widgetMap ;
			this.widgetsSortedByArea = widgetsSortedByArea ;
		}
	}
}
