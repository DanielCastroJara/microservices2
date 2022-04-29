package com.example.serviceproduct;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryMockTest {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void whenFindByCategorythenReturnListProduct() {
		Product product01 = Product.builder()
				.name("computer")
				.category(Category.builder().id(1L).build())
				.description("")
				.stock(Double.valueOf("10"))
				.price(Double.parseDouble("150"))
				.status("created")
				.createAt(new Date()).build();
		
		productRepository.save(product01);
		
		List<Product> result = productRepository.findByCategory(product01.getCategory());
		
		Assertions.assertThat(result.size()).isEqualTo(3);
	}

}
