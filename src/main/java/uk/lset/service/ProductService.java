package uk.lset.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.entities.Products;
import uk.lset.exception.ItemNotFoundException;
import uk.lset.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Products addNewProduct(Products products) {
        if(productRepository.existsByUpc(products.getUpc())) {
            throw new RuntimeException("Inventory ID already exists.");
        }

        return productRepository.save(products);
    }


    @Transactional(readOnly = true)
    public List<Products> getAllProducts(){

        return productRepository.findAll();
    }

	/*public Product getProductById(String id) {
		return productRepository.findById(id).orElse(null);
	}*/


    //Service for sorted products and pagination
    @Transactional(readOnly = true)
    public Page<Products> getAllProductsSorted(int page, int size, Sort sort, String category) {
        Pageable pageable = PageRequest.of(page, size, sort);
        if(category != null) {
            return productRepository.findByCategory(category, pageable);
        } else {
            return productRepository.findAll(pageable);
        }

    }



    public Optional<Products> getProductByProductId(String id) {
        return productRepository.findById(id);
    }

    public Products getProductById(String productId) {

        return productRepository.findByProductId(productId).orElseThrow(()
                -> new ItemNotFoundException("Product not found! " + "Check id: " + productId ));
    }


    public Products getProductByInventoryId(String upc) {
        return productRepository.findByUpc(upc).orElseThrow(() -> new ItemNotFoundException("Product not found! " + "Check id: " + upc ));
    }

    @Transactional
    public Products updateProduct(Products products) {
        if(!productRepository.existsByProductId(products.getProductId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product with id " + products.getProductId() + " does not exists." );
        }
        return productRepository.save(products);
    }

    @Transactional
    public void deleteProduct (String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id " + id + " does not exists.");
        }
        productRepository.deleteById(id);
    }
}
