package com.trainlab.repository;

import com.trainlab.model.testapi.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
