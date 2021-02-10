package com.miro.widgets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miro.widgets.entity.Widget;

public interface WidgetRepository extends JpaRepository<Widget, String> {
	Optional<Widget> findByZindex(int zIndex) ; 
	
	Optional<Widget> findFirstByOrderByZindex() ; 
}
