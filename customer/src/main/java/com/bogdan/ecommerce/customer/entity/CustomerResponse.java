package com.bogdan.ecommerce.customer.entity;

public record CustomerResponse(
    String id,
    String firstname,
    String lastname,
    String email,
    Address address
) {

}
