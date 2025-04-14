package uk.lset.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "inventory")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Inventory {
    @Id
    @UuidGenerator
    @Column(name = "inventory_Id", updatable = false, nullable = false)
    private String inventoryId;

    @Column(name = "quantity_available")
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantityAvailable;

    @Column(name = "minimum_stock_level")
    private final int minimumStockLevel = 1;

    @Column(name = "reorder_point")
    private final int reorderPoint = 1;

    @Column(name = "product_Id")
    private String productId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id", referencedColumnName = "product_id",insertable = false, updatable = false)
    private Products product;


}
