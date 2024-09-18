package com.bogdan.userservice.repository;

import com.bogdan.userservice.enums.Active;
import com.bogdan.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByActive(Active active);

}
