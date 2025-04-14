package uk.lset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.dto.ProductInventoryDTO;
import uk.lset.dto.UpdateProductStock;
import uk.lset.entities.Inventory;
import uk.lset.exception.ItemNotFoundException;
import uk.lset.exception.QuantityBadRequestException;
import uk.lset.repository.InventoryRepository;
import uk.lset.repository.ProductRepository;
import uk.lset.request.InventoryRequest;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }


    public ProductInventoryDTO getProductStock(String productId) {
            return inventoryRepository.getProductWithStock(productId).orElseThrow(()
                    ->new ItemNotFoundException("Product with id " + productId + " does not exists."));

    }

    public List<ProductInventoryDTO> getAllStocks() {
        return inventoryRepository.getAllProductsWithStock();
    }

    public Inventory getInventoryById(String id) {
        return inventoryRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Inventory with id " + id + " does not exists."));
    }

    @Transactional
    public Inventory addNewProductToInventory(@RequestBody InventoryRequest inventoryRequest, String productId) {
        if(inventoryRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("Product with id " + productId + " already exists in inventory.");
        } else if(!productRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("Product with id " + productId + " does not exists.");
        } else if(inventoryRequest.getQuantityAvailable() < 0) {
            throw new QuantityBadRequestException("Quantity must be greater than zero.");
        }
        Inventory inventory = new Inventory();
        inventory.setProductId(productId);
        inventory.setQuantityAvailable(inventoryRequest.getQuantityAvailable());

        return inventoryRepository.save(inventory);

    }

    public Inventory updateProductStock(@RequestBody InventoryRequest inventoryRequest,  String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new ItemNotFoundException("Product with id " + productId + " does not exist in inventory."));
        if(inventoryRequest.getQuantityAvailable() < 0) {
            throw new QuantityBadRequestException("Quantity must be greater than zero.");
        }
        inventory.setQuantityAvailable(inventoryRequest.getQuantityAvailable());
        return inventoryRepository.save(inventory);
    }

    public Inventory addProductStock(@RequestBody UpdateProductStock updateProductStock, String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new ItemNotFoundException("Product with id " + productId + " does not exist in inventory."));
        if(updateProductStock.getQuantityToAdd() < 0) {
            throw new QuantityBadRequestException("Quantity must be greater than zero.");
        }
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() + updateProductStock.getQuantityToAdd());
        return inventoryRepository.save(inventory);
    }


    public Inventory removeProductStock(@RequestBody InventoryRequest inventoryRequest, String productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() ->
                new ItemNotFoundException("Product with id " + productId + " does not exist in inventory."));
        inventory.setQuantityAvailable(inventory.getQuantityAvailable() - inventoryRequest.getQuantityAvailable());
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteProductStock(String productId) {
        if(!inventoryRepository.existsByProductId(productId)) {
            throw new ItemNotFoundException("Product with id " + productId + " does not exist in inventory.");
        }
        inventoryRepository.deleteByProductId(productId);
    }
}
