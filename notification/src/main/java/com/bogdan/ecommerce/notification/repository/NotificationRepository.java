package com.bogdan.ecommerce.notification.repository;

import com.bogdan.ecommerce.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
