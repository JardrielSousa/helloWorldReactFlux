package br.com;

import java.util.Arrays;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class DemoReactApplication {

	public static void main(String[] args) {
		Mono.just("hello world!!").subscribe(log::info);
		
		Flux.just(1,2,3,4,5).subscribe(i->log.info("number {}",i));
		
		Flux.range(1, 5).subscribe(i->log.info("using range {}",i));
		//convert of Flux to Mono
		Mono.from(Flux.range(1, 5)).subscribe(i->log.info("flux converted to Mono number {}",i));
		//convert of Mono to Flux
		Flux.from(Mono.just("hello world")).subscribe(log::info);
		//Itarator Using array
		Flux.fromArray(new Integer[] {1,3,6,7}).subscribe(i->log.info("from array: {}", i));
		//Itarator Using list
		Flux.fromIterable(Arrays.asList(2,4,6,8)).subscribe(i->log.info("from list: {}", i));
		
		//verify if object is just or empty
		Flux.from(Mono.justOrEmpty(Optional.ofNullable("hello"))).subscribe(e->log.info("just or empty: {}", e));
		
		// verify evens 
		boolean onlyEvens = true;
		Flux.defer(()-> onlyEvens?
				Flux.range(1,10).filter(p-> p % 2 == 0) : 
				Flux.range(1,10).filter(p-> p % 2 != 0))
					.subscribe(
							e-> log.info("elem: {}",e),
							error -> log.error("erro" + error),
							() -> log.info("complete"),
							//Control flux limiting the objects
							subscriptor -> {
								subscriptor.request(4);
								subscriptor.cancel();
							});

	}

}
