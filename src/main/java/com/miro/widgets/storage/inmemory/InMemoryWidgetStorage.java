package com.miro.widgets.storage.inmemory;

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

import com.miro.widgets.entity.Widget;
import com.miro.widgets.storage.WidgetStorage;

@Component
@ConditionalOnProperty(name = "storage", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryWidgetStorage implements WidgetStorage {
	
	private IndexOrganizer zIndexOrganizer = new IndexOrganizer() ;
	private Map<String, Widget> widgetMap = new HashMap<>() ;
	private ReentrantLock lock = new ReentrantLock() ;

	@Override
	public Optional<Widget> findById(String id) {
		return Optional.ofNullable(widgetMap.get(id));
	}

	@Override
	public List<Widget> findAll(Pageable pageable) {
		return widgetMap.values().stream()
				.sorted(Comparator.comparingInt(Widget::getZindex))
				.collect(Collectors.toList())
				.stream()
				.skip(pageable.getPageNumber() * pageable.getPageSize())
				.limit(pageable.getPageSize())
				.collect(Collectors.toList());
	}

	@Override
	public Widget create(Widget widget) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(widgetMap);
			
			if (widget.isZindexNotSpecified()) {
				widget.setZindex(zIndexOrganizer.getMaxIndex(updatedMap) + 1);
			} else {
				updatedMap = zIndexOrganizer.shiftIndexes(widget, updatedMap) ;
			}

			updatedMap.put(widget.getId(), widget) ;
			widgetMap = updatedMap ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public Widget update(Widget widget) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(widgetMap);
			
			if (zIndexHasBeenModified(widget, updatedMap)) {
				updatedMap.remove(widget.getId()) ;
				updatedMap = zIndexOrganizer.shiftIndexes(widget, updatedMap) ;
			}

			updatedMap.put(widget.getId(), widget) ;
			widgetMap = updatedMap ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public void deleteById(String id) {
		lock.lock(); 
		try {
			Map<String, Widget> updatedMap = new HashMap<>(widgetMap);
			updatedMap.remove(id) ;
			widgetMap = updatedMap ;
		} finally {
			lock.unlock(); 
		}
	}

	@Override
	public void deleteAll() {
		lock.lock(); 
		try {
			widgetMap = new HashMap<>() ;
		} finally {
			lock.unlock(); 
		}
	}

	private boolean zIndexHasBeenModified(Widget widget, Map<String, Widget> updatedMap) {
		return !updatedMap.get(widget.getId()).getZindex().equals(widget.getZindex()) ;
	}

}
