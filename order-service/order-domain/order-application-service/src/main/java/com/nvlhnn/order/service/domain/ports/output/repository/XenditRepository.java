package com.nvlhnn.order.service.domain.ports.output.repository;

import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.exception.XenditException;
import com.nvlhnn.order.service.domain.valueobject.Invoice;

public interface XenditRepository {
    Invoice createInvoice(Order order, User user) throws XenditException;
}
