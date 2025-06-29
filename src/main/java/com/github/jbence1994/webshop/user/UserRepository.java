package com.github.jbence1994.webshop.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByProfilePhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
}
