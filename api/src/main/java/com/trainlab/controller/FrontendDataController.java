package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import com.trainlab.repository.FrontendDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
public class FrontendDataController {
    private final FrontendDataRepository frontendDataRepository;

    @GetMapping("/main-page")
    public ResponseEntity<FrontendData> getMainPageData() {
        List<FrontendData> mainPageDataList = frontendDataRepository.findMainPageData();

        if (mainPageDataList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FrontendData mainPageData = mainPageDataList.get(0);

        return new ResponseEntity<>(mainPageData, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FrontendData>> getAllFrontendData() {
        List<FrontendData> frontendDataList = frontendDataRepository.findAll();

        return new ResponseEntity<>(frontendDataList, HttpStatus.OK);
    }
}
