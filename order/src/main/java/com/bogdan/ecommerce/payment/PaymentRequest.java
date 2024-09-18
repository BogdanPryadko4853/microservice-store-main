package com.bogdan.ecommerce.payment;

import com.bogdan.ecommerce.customer.CustomerResponse;
import com.bogdan.ecommerce.order.entity.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderReference,
    CustomerResponse customer
) {
}
