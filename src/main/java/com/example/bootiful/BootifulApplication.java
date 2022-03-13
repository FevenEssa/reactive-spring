package com.example.bootiful;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@SpringBootApplication
public class BootifulApplication {
	@Bean
	RouterFunction<ServerResponse> routes(CustomerRepository cr){
		System.out.println("Extracting all customers **********************");
		return RouterFunctions.route(GET("/customers"), request -> ok().body(cr.findAll(), Customer.class));
	}

	public static void main(String[] args) {
		SpringApplication.run(BootifulApplication.class, args);
	}

}
@Component
class DataWriter implements ApplicationRunner {
	private final CustomerRepository customerRepository;

	DataWriter(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		Flux.just("Mia", "Madhura", "Dave", "Onsi")
				.flatMap(name -> customerRepository.save(new Customer(null, name)))
				.subscribe(System.out::println);
	}
}
interface CustomerRepository extends ReactiveMongoRepository<Customer, String>{}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
	private String id, name;
}