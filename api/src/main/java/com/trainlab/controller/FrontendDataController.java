package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import com.trainlab.model.User;
import com.trainlab.repository.FrontendDataRepository;
import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final SessionService sessionService;

    private final RandomValuesGenerator randomValuesGenerator;

    @GetMapping("/main-page")
    public ResponseEntity<Map<String, String>> getMainPageData() {
        Long userId = getUserIdFromContext();

        String sessionToken = randomValuesGenerator.uuidGenerator();
        sessionService.createSessionForUser(sessionToken, userId);

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

        Long userId = getUserIdFromContext();
        String sessionToken = randomValuesGenerator.uuidGenerator();
        sessionService.createSessionForUser(sessionToken, userId);

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

    private Long getUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return ((User) principal).getId();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
