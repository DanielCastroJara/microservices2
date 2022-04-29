package com.example.serviceproduct;

import java.util.Date;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.repository.ProductRepository;
import com.example.serviceproduct.service.ProductService;
import com.example.serviceproduct.service.ProductServiceImpl;

@SpringBootTest
public class ProductServiceMockTest {
	
	@Mock
	private ProductRepository productRepository;

	private ProductService productService;
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		productService = new ProductServiceImpl(productRepository);
		Product product01 = Product.builder()
				.name("computer")
				.id(1L)
				.category(Category.builder().id(1L).build())
				.description("")
				.stock(Double.valueOf("5"))
				.price(Double.parseDouble("12.5"))
				.status("created")
				.createAt(new Date()).build();
		
		Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product01));
		
		Mockito.when(productRepository.save(product01)).thenReturn(product01);
		
	}
	
	@Test
	public void whenValidGetID_ThenReturnProduct() {
		 Product result = productService.getProduct(1L);
		 Assertions.assertThat(result.getName()).isEqualTo("computer");
	}
	
	@Test
	public void whenValidateUpdateStock_ThenReturnNewStock() {
		Product newStock = productService.updateStock(1L, Double.valueOf("8"));
		Assertions.assertThat(newStock.getStock()).isEqualTo(13);
	}
}
