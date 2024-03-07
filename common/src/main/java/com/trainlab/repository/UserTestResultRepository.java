package com.trainlab.repository;

import com.trainlab.model.testapi.UserTestResult;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserTestResultRepository extends JpaRepository<UserTestResult,Long> {
}
