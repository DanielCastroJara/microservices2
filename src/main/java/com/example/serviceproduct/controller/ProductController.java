package com.example.serviceproduct.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<List<Product>> listProducts(@RequestParam(name = "categoryId", required = false) Long categoryId) {
		List<Product> result = new ArrayList<Product>();
		if (categoryId == null) {
			result = productService.listAllProduct();
			if (result.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
		} else {
			result = productService.findByCategory(Category.builder().id(categoryId).build());
			if (result.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
		}
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
		Product product = productService.getProduct(id);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(product);
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
		}
		Product productCreated = productService.createProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id,@RequestBody Product product) {
		product.setId(id);
		Product producDB = productService.updateProduct(product);
		if (producDB == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(producDB);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id) {
		Product productToDelete = productService.deleteProduct(id);
		if (productToDelete == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(productToDelete);
	}
	
	@PutMapping(value = "/{id}/stock")
	public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam(name = "quantity", required = true) Double quantity) {
		Product product = productService.updateStock(id, quantity);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(product);
	}
	
	private String formatMessage(BindingResult result) {
		List<Map<String, String>> errors = result.getFieldErrors().stream()
				.map(err -> {
					Map<String, String> error = new HashMap<>();
					error.put(err.getField(), err.getDefaultMessage());
					return error;
				}).collect(Collectors.toList());
		ErrorMessage errorMessage = ErrorMessage.builder()
				.code("01")
				.messages(errors).build();
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder jsonString = new StringBuilder();
		try {
			jsonString.append(mapper.writeValueAsString(errorMessage));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString.toString();
	}

}
