package com.trainlab.repository;

import com.trainlab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAuthenticationInfoEmail(String email);

    List<User> findAllByIsDeletedFalseOrderById();
    User findByIsDeletedFalseAndId(Long id);
}
