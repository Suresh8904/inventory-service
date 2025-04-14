package uk.lset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.entities.Products;
import uk.lset.service.ProductService;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //Add new Product

    @PostMapping(path = "/products/addNewProduct")
    public ResponseEntity<?> addNewProduct(@RequestBody Products products){
        try {
            return ResponseEntity.ok(productService.addNewProduct(products));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error" + e.getMessage());
        }
    }


    //Request all products

    @GetMapping(path = "/products/all")
    public ResponseEntity<List<Products>> getAllProducts(){
        List<Products> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }



    //Get all products sorted
    //products/sort?sortBy=" "&ascending=" "
    @GetMapping(path = "/products/sort")
    public Page<Products> sortProducts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam (required = false) String category,
                                       @RequestParam(defaultValue = "name") String sortBy,
                                       @RequestParam(defaultValue = "true") boolean ascending) {

        Sort sort = ascending? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return productService.getAllProductsSorted(page, size, sort, category);
    }


    //get products by productId
    @GetMapping(path = "/products/productId/{id}")
    public ResponseEntity<Products> productsById(@PathVariable String id){
        Products products = productService.getProductById(id);
        return ResponseEntity.ok(products);
    }


    //get products by inventoryId
    @GetMapping("/products/inventoryId/{inventoryId}")
    public ResponseEntity<Products> productsByInventoryId(@PathVariable String inventoryId){
        Products products = productService.getProductByInventoryId(inventoryId);
        return ResponseEntity.ok(products);
    }

    //Update product
    @PutMapping(path = "/products/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody Products products, @PathVariable String id) {
        try {
            Optional<Products> productOptional = productService.getProductByProductId(id);
            if (productOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }
            Products updatedProducts = null;
            Products productsToUpdate = productOptional.get();
            productsToUpdate.setProductName(products.getProductName());
            productsToUpdate.setProductDescription(products.getProductDescription());
            productsToUpdate.setCategory(products.getCategory());
            productsToUpdate.setPrice(products.getPrice());
            productsToUpdate.setUpc(products.getUpc());
            updatedProducts = productService.updateProduct(productsToUpdate);
            return ResponseEntity.ok(updatedProducts);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product. " + e.getMessage());
        }
    }



    //Delete product by supplierCode
    @DeleteMapping(path = "/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product. " + e.getMessage());
        }
    }
}
