package com.trainlab.repository;

import com.trainlab.model.FrontendData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FrontendDataRepository extends JpaRepository<FrontendData, Integer> {
    FrontendData findByFrontId(float frontId);

    @Query(value = "SELECT CONCAT('\"', front_id, '\"', ':', '\"', text, '\"') FROM frontend_data WHERE front_id LIKE '1%'", nativeQuery = true)
    List<String> findMainPageData();
}