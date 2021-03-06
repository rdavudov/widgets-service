package com.miro.widgets.storage.database;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.miro.widgets.dto.RegionDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.repository.WidgetRepository;
import com.miro.widgets.storage.WidgetStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage", havingValue = "database")
@Slf4j
public class JdbcWidgetStorage implements WidgetStorage {
	private final WidgetRepository widgetRepository ;
	private final JdbcIndexOrganizer indexOrganizer ;
	private ReentrantLock lock = new ReentrantLock() ;

	@Override
	public Optional<Widget> findById(String id) {
		return widgetRepository.findById(id);
	}

	@Override
	public List<Widget> findAll(Pageable pageable) {
		return widgetRepository.findAllByOrderByZindex(pageable).toList() ;
	}
	
	@Override
	public List<Widget> findAllByRegion(RegionDto region) {
		return widgetRepository.findAllByArea(region.getX(), region.getY(), region.getWidth(), region.getHeight());
	}

	@Override
	public Widget create(Widget widget) {
		lock.lock(); 
		try {
			if (widget.isZindexNotSpecified()) {
				widget.setZindex(indexOrganizer.getMaxIndex() + 1);
				log.info("widget {} has no z index using max value {}", widget.getId(), widget.getZindex());
			} else {
				log.info("shifting other widgets");
				indexOrganizer.shiftIndexes(widget);
			}

			widgetRepository.save(widget) ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public Widget update(Widget widget) {
		lock.lock(); 
		try {
			if (zIndexHasBeenModified(widget)) {
				log.info("widget has modified z index");
				log.info("shifting other widgets");
				indexOrganizer.shiftIndexes(widget);
			}
			
			widgetRepository.save(widget) ;
		} finally {
			lock.unlock(); 
		}
		return widget ;
	}

	@Override
	public void deleteById(String id) {
		widgetRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		widgetRepository.deleteAll();
	}
	
	private boolean zIndexHasBeenModified(Widget widget) {
		return !widgetRepository.findById(widget.getId()).get().getZindex().equals(widget.getZindex()) ;
	}

}
