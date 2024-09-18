package com.bogdan.ecommerce.customer;

public record CustomerResponse(
    String id,
    String firstname,
    String lastname,
    String email
) {

}
