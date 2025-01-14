package com.nvlhnn.order.service.dataaccess.xendit.adapter;

import com.nvlhnn.order.service.dataaccess.xendit.mapper.XenditDataAccessMapper;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.exception.XenditException;
import com.nvlhnn.order.service.domain.ports.output.repository.XenditRepository;
import com.nvlhnn.order.service.domain.valueobject.Invoice;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class XenditRepositoryImpl implements XenditRepository {

    private final XenditDataAccessMapper xenditDataAccessMapper;

    public XenditRepositoryImpl(XenditDataAccessMapper xenditDataAccessMapper) {
        this.xenditDataAccessMapper = xenditDataAccessMapper;
    }

    @Override
    public Invoice createInvoice(Order order, User user) throws XenditException {
        try {
            // Map order and user to InvoiceEntity
            var invoiceEntity = xenditDataAccessMapper.orderToXenditInvoice(order, user);

            // Prepare parameters for the Xendit API
            var params = new HashMap<String, Object>();
            params.put("external_id", invoiceEntity.getExternalId());
            params.put("amount", invoiceEntity.getAmount());
            params.put("payer_email", invoiceEntity.getPayerEmail());
            params.put("description", invoiceEntity.getDescription());
            params.put("success_redirect_url", invoiceEntity.getSuccessRedirectUrl());
            params.put("failure_redirect_url", invoiceEntity.getFailureRedirectUrl());
            params.put("invoice_duration", 86400);

            // Call Xendit API
            com.xendit.model.Invoice xenditInvoice = com.xendit.model.Invoice.create(params);

            // Map XenditLibInvoice to domain Invoice
            return xenditDataAccessMapper.xenditInvoiceToInvoice(xenditInvoice);

        } catch (com.xendit.exception.XenditException e) { // Fully qualified class name
            throw new XenditException("Failed to create Xendit invoice", e);
        }
    }
}
