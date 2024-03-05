package com.trainlab.repository;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.model.testapi.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllBySpecialty(eSpecialty specialty);
}
