package com.example.tvmanagerment.Service;

import com.example.tvmanagerment.Model.Product;
import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Repository.ProductCategoryRepository;
import com.example.tvmanagerment.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCategoryRepository productCategory;

    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByCreatedAtProductDesc();
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public Product saveOrUpdateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public ProductCategory findCategoryById(int categoryId) {
        return productCategory.findById(categoryId).orElse(null);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);

    }

    public List<Product> searchProductName(String productName) {
        return productRepository.findByProductNameContaining(productName);
    }

    public List<Product> findAllProductsSortedByPriceAsc() {
      List<Product> sortedProductsAsc = productRepository.findAllByOrderByPriceAsc();
        productRepository.saveAll(sortedProductsAsc);
        return sortedProductsAsc;
    }


    public List<Product> findAllProductsSortedByPriceDesc() {
        List<Product> sortedProductsDesc = productRepository.findAllByOrderByPriceDesc();
        productRepository.saveAll(sortedProductsDesc);
        return sortedProductsDesc;
    }

}

