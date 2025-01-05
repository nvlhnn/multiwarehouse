package com.nvlhnn.warehouse.service.dataaccess.orderStockMutation.entity;

import com.nvlhnn.warehouse.service.domain.valueobject.StatusStockMutation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_stock_mutations")
@Entity
public class OrderStockMutationEntity {
    @Id
    private UUID id;
    private UUID orderId;
    private UUID warehouseId;
    private UUID productId;

    @Enumerated(EnumType.STRING)
    private StatusStockMutation statusStockMutation;

    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStockMutationEntity that = (OrderStockMutationEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
