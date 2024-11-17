package com.trainlab.repository;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.model.User;
import com.trainlab.model.testapi.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    UserStats findByUserAndAndSpecialty(User user, eSpecialty specialty);

    List<UserStats> findByUser(User user);
}
