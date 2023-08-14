package com.trainlab.controller;

import com.trainlab.service.FrontendDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
public class FrontendDataControllerImpl implements FrontendDataController {
    private final FrontendDataService frontendDataService;

    @Override
    @GetMapping("/pages/{page}")
    public ResponseEntity<Map<String, String>> getMainPageData(@PathVariable Integer page) {
        Map<String, String> mainPageDataMap = frontendDataService.getDataByPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(mainPageDataMap);
    }
}
