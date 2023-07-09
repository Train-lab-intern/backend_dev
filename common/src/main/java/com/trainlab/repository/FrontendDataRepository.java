package com.trainlab.repository;

import com.trainlab.model.FrontendData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrontendDataRepository extends JpaRepository<FrontendData, Integer> {
    FrontendData findByFrontId(float frontId);
}