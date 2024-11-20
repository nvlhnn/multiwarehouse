package com.nvlhnn.order.service.dataaccess.customer.mapper;

import com.nvlhnn.domain.valueobject.CustomerId;
import com.nvlhnn.order.service.dataaccess.customer.entity.CustomerEntity;
import com.nvlhnn.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}