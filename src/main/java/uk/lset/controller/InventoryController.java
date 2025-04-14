package uk.lset.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.lset.dto.ProductInventoryDTO;
import uk.lset.dto.UpdateProductStock;
import uk.lset.entities.Inventory;
import uk.lset.request.InventoryRequest;
import uk.lset.service.InventoryService;


import java.util.List;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @GetMapping("/inventory/all")
    public ResponseEntity<List<ProductInventoryDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllStocks());
    }
//
    @GetMapping(path = "/inventory/inventoryByProductId/{productId}")
    public ResponseEntity<ProductInventoryDTO> getProductStock(@PathVariable String productId) {
        return ResponseEntity.ok(inventoryService.getProductStock(productId));
    }


    @PostMapping("/inventory/addProductToInventory/{productId}")
    public ResponseEntity<Inventory> addProductToInventory(@RequestBody InventoryRequest inventoryRequest, @PathVariable String productId) {
        Inventory inventory = inventoryService.addNewProductToInventory(inventoryRequest, productId);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @PutMapping("/inventory/updateProductStock/{productId}")
    public ResponseEntity<Inventory>updateProductStock(@RequestBody InventoryRequest inventoryRequest, @PathVariable String productId) {
        Inventory inventory = inventoryService.updateProductStock(inventoryRequest,productId);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/inventory/addProductStock/{productId}")
    public ResponseEntity<Inventory> addProductStock(@RequestBody UpdateProductStock updateProductStock, @PathVariable String productId) {
        Inventory inventory = inventoryService.addProductStock( updateProductStock,productId);
        return ResponseEntity.ok(inventory);
    }

    @DeleteMapping("inventory/deleteInventoryByProductId/{productId}")
    public ResponseEntity<String> deleteInventoryByProductId(@PathVariable String productId) {
        try
        {
            inventoryService.deleteProductStock(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product. " + e.getMessage());
        }
    }
}