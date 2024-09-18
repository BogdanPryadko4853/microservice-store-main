package com.bogdan.ecommerce.handler;

import java.util.Map;

public record ErrorResponse(
    Map<String, String> errors
) {

}
