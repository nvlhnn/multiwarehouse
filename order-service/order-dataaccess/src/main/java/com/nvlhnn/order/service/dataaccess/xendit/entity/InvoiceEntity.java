package com.nvlhnn.order.service.dataaccess.xendit.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class InvoiceEntity {
    private String externalId;
    private Number amount;
    private String payerEmail;
    private String description;
    private String successRedirectUrl;
    private String failureRedirectUrl;
}
