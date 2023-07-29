package com.trainlab.repository;

import com.trainlab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//@Cacheable("users")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthenticationInfoEmail(String email);

}
