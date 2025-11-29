package com.blockflow;

import com.blockflow.model.Product;
import com.blockflow.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class BlockflowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockflowBackendApplication.class, args);
	}

	@Bean
	@SuppressWarnings("null")
	CommandLineRunner initData(ProductRepository productRepository) {
		return args -> {
			try {
				if (productRepository.count() == 0) {
					Product p1 = Product.builder()
							.name("Standard AAC Block")
							.dimensions("600x200x100mm")
							.pricePerUnit(new BigDecimal("45.00"))
							.stockQuantity(500)
							.description("Standard block for general construction")
							.weight(new BigDecimal("7.50"))
							.build();

					Product p2 = Product.builder()
							.name("Jumbo AAC Block")
							.dimensions("600x200x150mm")
							.pricePerUnit(new BigDecimal("65.00"))
							.stockQuantity(300)
							.description("Larger block for load bearing walls")
							.weight(new BigDecimal("11.00"))
							.build();

					Product p3 = Product.builder()
							.name("Partition AAC Block")
							.dimensions("600x200x75mm")
							.pricePerUnit(new BigDecimal("35.00"))
							.stockQuantity(400)
							.description("Thinner block for partition walls")
							.weight(new BigDecimal("5.50"))
							.build();

					Product p4 = Product.builder()
							.name("U-Shape AAC Block")
							.dimensions("600x200x200mm")
							.pricePerUnit(new BigDecimal("80.00"))
							.stockQuantity(200)
							.description("U-shaped block for bond beams")
							.weight(new BigDecimal("14.00"))
							.build();

					Product p5 = Product.builder()
							.name("Lintel AAC Block")
							.dimensions("600x150x100mm")
							.pricePerUnit(new BigDecimal("50.00"))
							.stockQuantity(350)
							.description("Block for door and window lintels")
							.weight(new BigDecimal("6.00"))
							.build();

					List<Product> products = List.of(p1, p2, p3, p4, p5);
					productRepository.saveAll(products);
					System.out.println("Sample products initialized successfully.");
				} else {
					System.out.println("Products already exist, skipping initialization.");
				}
			} catch (Exception e) {
				System.err.println("Error initializing data: " + e.getMessage());
				e.printStackTrace();
			}
		};
	}

}
