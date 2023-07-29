package com.trainlab.controller;

import com.trainlab.model.FrontendData;
import com.trainlab.model.User;
import com.trainlab.repository.FrontendDataRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.SessionService;
import com.trainlab.util.RandomValuesGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/front")
@RequiredArgsConstructor
@Slf4j
public class FrontendDataController {
    private final UserRepository userRepository;
    private final FrontendDataRepository frontendDataRepository;

    private final SessionService sessionService;

    private final RandomValuesGenerator randomValuesGenerator;

    @GetMapping("/pages/{range}")
    public ResponseEntity<Map<String, String>> getMainPageData(@PathVariable int range, Principal principal) {

        Long userId = null;

        if (principal != null) {
            Optional<User> userOptional = userRepository.findByAuthenticationInfoEmail(principal.getName());

            if (userOptional.isPresent()) {
                userId = userOptional.get().getId();
            }
        }

        String sessionToken = randomValuesGenerator.uuidGenerator();
        sessionService.createSession(sessionToken, userId);

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
