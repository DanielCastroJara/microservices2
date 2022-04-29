package com.example.serviceproduct.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.serviceproduct.entity.Category;
import com.example.serviceproduct.entity.Product;
import com.example.serviceproduct.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
	
	
	private final ProductRepository productRepository;

	@Override
	public List<Product> listAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public Product getProduct(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product createProduct(Product product) {
		product.setStatus("CREATED");
		product.setCreateAt(new Date());
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Product product) {
		Product productBD = getProduct(product.getId());
		if(null == productBD) {
			return null;
		}
		productBD.setName(product.getName());
		productBD.setCategory(product.getCategory());
		productBD.setDescription(product.getDescription());
		productBD.setPrice(product.getPrice());
		
		return productRepository.save(productBD);
	}

	@Override
	public Product deleteProduct(Long id) {
		Product productBD = getProduct(id);
		if(null == productBD) {
			return null;
		}
		productBD.setStatus("DELETED");
		return productRepository.save(productBD);
	}

	@Override
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public Product updateStock(Long id, Double quantity) {
		Product productBD = getProduct(id);
		if(null == productBD) {
			return null;
		}
		productBD.setStock(productBD.getStock() + quantity);
		return productRepository.save(productBD);
	}

}
