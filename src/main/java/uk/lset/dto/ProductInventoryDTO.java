package uk.lset.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInventoryDTO {
    private String productId;
    private String productName;
    private int quantityAvailable;

    public ProductInventoryDTO(String productId, String productName, int quantityAvailable) {
        this.productId = productId;
        this.productName = productName;
        this.quantityAvailable = quantityAvailable;
    }
}
