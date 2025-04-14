package uk.lset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.lset.dto.ProductInventoryDTO;
import uk.lset.entities.Inventory;


import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String> {

    @Query(value = "SELECT new uk.lset.dto.ProductInventoryDTO(p.productId, p.productName, i.quantityAvailable) " +
            "FROM Inventory i JOIN i.product p WHERE p.productId = :productId")
    Optional<ProductInventoryDTO> getProductWithStock(String productId);


    @Query(value = "SELECT new uk.lset.dto.ProductInventoryDTO(p.productId, p.productName, i.quantityAvailable) " +
            "FROM Inventory i JOIN i.product p")
    List<ProductInventoryDTO> getAllProductsWithStock();

    boolean existsByProductId(String productId);

    Optional<Inventory>findByProductId(String productId);

    void deleteByProductId(String productId);
}
