package com.nvlhnn.order.service.domain.valueobject;

import java.util.Objects;

public class Invoice {
    private final String id;
    private final String invoiceUrl;

    public Invoice(String id, String invoiceUrl) {
        this.id = id;
        this.invoiceUrl = invoiceUrl;
    }

    public String getId() {
        return id;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id) && Objects.equals(invoiceUrl, invoice.invoiceUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoiceUrl);
    }
}
