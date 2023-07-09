package com.trainlab.repository;

import com.trainlab.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
    Optional<TestEntity> findById(Long id);
}