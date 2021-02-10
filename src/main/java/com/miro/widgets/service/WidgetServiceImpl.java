package com.miro.widgets.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miro.widgets.dto.WidgetDto;
import com.miro.widgets.entity.Widget;
import com.miro.widgets.storage.WidgetStorage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WidgetServiceImpl implements WidgetService {

	private final WidgetStorage widgetStorage ;
	
	@Override
	public Optional<Widget> findById(String id) {
		return widgetStorage.findById(id);
	}

	@Override
	public List<Widget> findAll(Pageable pageable) {
		return widgetStorage.findAll(pageable) ;
	}

	@Override
	@Transactional
	public Widget create(WidgetDto dto) {
		Widget widget = Widget.buildFrom(dto) ;
		widget.setId(generateRandomId());
		
		return widgetStorage.create(widget);
	}

	@Override
	@Transactional
	public Widget update(String id, WidgetDto dto) {
		Widget widget = Widget.buildFrom(dto) ;
		widget.setId(id);
		
		return widgetStorage.update(widget);
	}

	@Override
	@Transactional
	public void deleteById(String id) {
		widgetStorage.deleteById(id);
	}
	
	private String generateRandomId() {
		return UUID.randomUUID().toString() ;
	}
}
