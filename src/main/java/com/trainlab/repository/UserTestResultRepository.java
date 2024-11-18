package com.trainlab.repository;

import com.trainlab.model.User;
import com.trainlab.model.testapi.UserTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserTestResultRepository extends JpaRepository<UserTestResult,Long> {

List<UserTestResult> findAllByUser(User user);
}
