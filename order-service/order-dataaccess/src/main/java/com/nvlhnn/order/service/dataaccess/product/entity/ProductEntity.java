    package com.nvlhnn.order.service.dataaccess.product.entity;

    import com.nvlhnn.order.service.dataaccess.order.entity.OrderEntity;
    import com.nvlhnn.order.service.dataaccess.order.entity.OrderItemEntity;
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
    @Table(name = "products")
    @Entity
    public class ProductEntity {
        @Id
        private UUID id;
        private String name;
        private BigDecimal price;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductEntity that = (ProductEntity) o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
