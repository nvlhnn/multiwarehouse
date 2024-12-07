package com.nvlhnn.dataaccess.inventory.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(InventoryEntityId.class)
@Table(name = "warehouse_product", schema = "warehouse")
@Entity
public class InventoryEntity {

    @Id
    private UUID warehouseId;
    @Id
    private UUID productId;
    private String warehouseName;
    private String productName;
    private String warehouseAddress;
    private Double warehouseLatitude;
    private Double warehouseLongitude;
    private Integer stock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntity that = (InventoryEntity) o;
        return warehouseId.equals(that.warehouseId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, productId);
    }
}
