package com.example.tvmanagerment.Controller;

import com.example.tvmanagerment.Model.Product;
import com.example.tvmanagerment.Model.ProductCategory;
import com.example.tvmanagerment.Model.ProductDTO;
import com.example.tvmanagerment.Service.ProductCategoryService;
import com.example.tvmanagerment.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {

        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        ProductCategory category = productService.findCategoryById(productDTO.getCategoryId());
        product.setCategory(category);
        Product savedProduct = productService.saveOrUpdateProduct(product);
        return ResponseEntity.ok(savedProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setPrice(productDTO.getPrice());

        if (productDTO.getCategoryId() != 0) {
            ProductCategory category = productCategoryService.getCategoryById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product category not found"));
            existingProduct.setCategory(category);
        }

        if (productDTO.getImageUrl() != null && !productDTO.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(productDTO.getImageUrl());
        }

        Product updatedProduct = productService.saveOrUpdateProduct(existingProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/pagination")
    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size
                                     ){
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductBySearch(@RequestParam(required = false, defaultValue = "") String search) {
        List<Product> productName = productService.searchProductName(search);
        if (productName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productName, HttpStatus.OK);
    }

    @GetMapping("/ascending")
    public List<Product> getProductsSortedByPriceAscending() {
        return productService.findAllProductsSortedByPriceAsc();
    }

    @GetMapping("/descending")
    public List<Product> getProductsSortedByPriceDescending() {
        return productService.findAllProductsSortedByPriceDesc();
    }
}

