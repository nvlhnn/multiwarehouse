package com.nvlhnn.warehouse.service.dataaccess.product.repository;

import com.nvlhnn.warehouse.service.dataaccess.product.entity.ProductEntity;
import com.nvlhnn.warehouse.service.dataaccess.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {


}
