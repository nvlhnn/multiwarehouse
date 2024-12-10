package com.nvlhnn.warehouse.service.domain.ports.input.service;


import com.nvlhnn.warehouse.service.domain.dto.create.*;

import javax.validation.Valid;

public interface WarehouseApplicationService {

    CreateWarehouseResponse createWarehouse(@Valid CreateWarehouseCommand createWarehouseCommand);

    CreateWarehouseResponse updateWarehouse(@Valid CreateUpdateWarehouseCommand updateWarehouseCommand);

    WarehouseListResponse listWarehouses(int page, int size);

//    AssignAdminResponse assignWarehouseAdmin(@Valid AssignAdminCommand assignAdminCommand);

    CreateTransferStockResponse transferStock(@Valid CreateTransferStockCommand stockTransferCommand);

    CreateStockResponse createStock(@Valid CreateStockCommand createStockCommand);

    CreateStockResponse updateStock(@Valid CreateUpdateStockCommand updateSTockCommand);

}
