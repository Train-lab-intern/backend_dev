package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import com.trainlab.repository.FrontendDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
public class FrontendDataController {
    private final FrontendDataRepository frontendDataRepository;

    @GetMapping("/main-page")
    public ResponseEntity<Map<String, String>> getMainPageData() {
        List<String> mainPageDataList = frontendDataRepository.findMainPageData();

        if (mainPageDataList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> mainPageDataMap = new HashMap<>();
        for (String data : mainPageDataList) {
            String[] parts = data.split(":");
            if (parts.length == 2) {
                String key = parts[0].replaceAll("\"", "").trim();
                String value = parts[1].replaceAll("\"", "").trim();
                mainPageDataMap.put(key, value);
            }
        }

        return new ResponseEntity<>(mainPageDataMap, HttpStatus.OK);
    }

    @GetMapping("/pages/{range}")
    public ResponseEntity<Map<String, String>> getMainPageData(@PathVariable int range) {
        List<FrontendData> mainPageDataList = frontendDataRepository.findDataByRange(range);

        if (mainPageDataList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> mainPageDataMap = new HashMap<>();
        for (FrontendData data : mainPageDataList) {
            mainPageDataMap.put(Float.toString(data.getFrontId()), data.getText());
        }

        return new ResponseEntity<>(mainPageDataMap, HttpStatus.OK);
    }
}
