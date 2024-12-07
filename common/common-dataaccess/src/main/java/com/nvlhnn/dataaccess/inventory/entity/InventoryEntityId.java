package com.nvlhnn.dataaccess.inventory.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntityId implements Serializable {

    private UUID warehouseId;
    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntityId that = (InventoryEntityId) o;
        return warehouseId.equals(that.warehouseId) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, productId);
    }
}
