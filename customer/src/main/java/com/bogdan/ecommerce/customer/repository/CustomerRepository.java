package com.bogdan.ecommerce.customer.repository;

import com.bogdan.ecommerce.customer.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String > {

}
