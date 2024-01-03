package com.trainlab.repository;

import com.trainlab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthenticationInfoEmailAndIsDeletedFalse(String email);

    Optional<User> findByAuthenticationInfoEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByIsDeletedFalseOrderById();

    Optional<User> findByIdAndIsDeletedFalse(Long id);

    Optional<User> findById(Long id);
}
