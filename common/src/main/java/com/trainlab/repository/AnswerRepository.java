package com.trainlab.repository;


import com.trainlab.model.testapi.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
