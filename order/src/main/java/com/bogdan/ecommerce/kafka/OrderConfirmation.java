package com.bogdan.ecommerce.kafka;

import com.bogdan.ecommerce.customer.CustomerResponse;
import com.bogdan.ecommerce.order.entity.PaymentMethod;
import com.bogdan.ecommerce.product.model.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}
