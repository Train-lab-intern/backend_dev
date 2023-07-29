package com.trainlab.repository;

import com.trainlab.model.FrontendData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FrontendDataRepository extends JpaRepository<FrontendData, Integer> {

    @Query(value = "SELECT * FROM frontend_data WHERE front_id >= ?1 AND front_id < ?1 + 1", nativeQuery = true)
    List<FrontendData> findDataByRange(int range);
}