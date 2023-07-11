package com.trainlab.repository;

import com.trainlab.model.FrontendData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FrontendDataRepository extends JpaRepository<FrontendData, Integer> {
    @Query("SELECT f.text FROM FrontendData f WHERE f.frontId = :frontId")
    String findTextByFrontId(@Param("frontId") float frontId);
}