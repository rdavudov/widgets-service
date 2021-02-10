package com.miro.widgets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.miro.widgets.entity.Widget;

public interface WidgetRepository extends JpaRepository<Widget, String> {
	Page<Widget> findAllByOrderByZindex(Pageable pageable) ;
	
	Optional<Widget> findByZindex(int zIndex) ; 
	
	Optional<Widget> findFirstByOrderByZindexDesc() ; 
	
	@Query("SELECT w FROM Widget w WHERE w.height * w.width <= :w * :h "
			+ "AND w.x >= :x AND w.x <= :x + :w "
			+ "AND w.x + w.width >= :x AND w.x + w.width <= :x + :w "
			+ "AND w.y >= :y AND w.y <= :y + :h "
			+ "AND w.y + w.height >= :y AND w.y + w.height <= :y + :h "
			)
	List<Widget> findAllByArea(@Param("x") int x, @Param("y") int y, @Param("w") int width, @Param("h") int height) ;
}
