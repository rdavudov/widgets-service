package com.miro.widgets.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.miro.widgets.entity.Widget;
import com.miro.widgets.repository.WidgetRepository;
import com.miro.widgets.storage.WidgetStorage;

@SpringBootTest(properties = "storage=database")
@Transactional
public class JdbcWidgetStoreTests {
	
	@Autowired
	private WidgetRepository widgetRepository ;
	
	@Autowired
	private WidgetStorage storage ;
	
	@BeforeEach
	public void setUp() {
		widgetRepository.deleteAll();
	}
	
	@Test
	public void given123WhenNew2Then1234() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.create(Widget.builder().id("x").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("x").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(4) ;
	}
	
	@Test
	public void given123WhenUpdate2Then123() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.update(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(3) ;
	}
	
	@Test
	public void given123WhenUpdate2as3Then134() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.update(Widget.builder().id("b").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(4) ;
	}
	
	@Test
	public void given123WhenNewThen1234() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.create(Widget.builder().id("x").x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("x").get().getZindex()).isEqualTo(4) ;
	}
	
	@Test
	public void given156WhenNew2Then1256() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(5).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(6).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.create(Widget.builder().id("x").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("x").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(5) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(6) ;
	}
	
	@Test
	public void given124WhenNew2Then1234() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(4).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.create(Widget.builder().id("x").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("x").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(4) ;
	}
	
	@Test
	public void given123WhenUpdate2to3Then134() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.create(Widget.builder().id("b").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("c").get().getZindex()).isEqualTo(4) ;
	}
	
	@Test
	public void given1234WhenDelete3AndNew2Then123() {
		storage.create(Widget.builder().id("a").zindex(1).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("b").zindex(2).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("c").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		storage.create(Widget.builder().id("d").zindex(4).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		storage.deleteById("c");
		storage.create(Widget.builder().id("x").zindex(3).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build()) ;
		
		assertThat(storage.findById("a").get().getZindex()).isEqualTo(1) ;
		assertThat(storage.findById("b").get().getZindex()).isEqualTo(2) ;
		assertThat(storage.findById("x").get().getZindex()).isEqualTo(3) ;
		assertThat(storage.findById("c")).isEmpty() ;
	}
	
	@Test
	public void givenRandomWhenConcurrentlyCreatedThenNoException() {
		int repeats = 10 ;
		int threads = 10 ;
		CyclicBarrier barrier = new CyclicBarrier(threads) ;
		
		Runnable run = new Runnable() {
			public void run() {
				try {
					barrier.await();
					Random random = new Random() ;
					for (int i = 0; i < repeats; i++) {
						Widget widget = Widget.builder().id(Thread.currentThread().getName() + "-" + i).zindex(random.nextInt(5)).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build() ;
						storage.create(widget) ;
					}
					
					for (int i = 0; i < repeats; i++) {
						Widget widget = Widget.builder().id(Thread.currentThread().getName() + "-" + i).zindex(random.nextInt(5)).x(10).y(10).height(10).width(10).lastModificationDate(LocalDateTime.now()).build() ;
						storage.update(widget) ;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread[] threadArray = new Thread[threads] ;
		for (int i = 0; i < threads; i++) {
			threadArray[i] = new Thread(run) ;
			threadArray[i].start();
		}
		
		for (int i = 0; i < threads; i++) {
			try {
				threadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertThat(storage.findAll(PageRequest.of(0, 500)).size()).isEqualTo(repeats * threads) ;
		assertThat(storage.findAll(PageRequest.of(0, 500)).stream().map(w -> w.getZindex()).collect(Collectors.toSet()).size()).isEqualTo(repeats * threads) ;
	}
}
