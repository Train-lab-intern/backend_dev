package com.trainlab.repository;

import com.trainlab.model.TrainlabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//@Cacheable("users")
public interface UserRepository extends JpaRepository<TrainlabUser, Long> {
    @Query(value = "SELECT * FROM users WHERE email LIKE %:email%", nativeQuery = true)
    List<TrainlabUser> findUsersByEmail(@Param("email") String email);

    Optional<TrainlabUser> findByAuthenticationInfoEmail(String email);

}
