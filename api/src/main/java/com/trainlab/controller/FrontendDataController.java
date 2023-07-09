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

@Controller
@RequestMapping("/front")
@RequiredArgsConstructor
public class FrontendDataController {
    private final FrontendDataRepository frontendDataRepository;

    @GetMapping("/{frontId}")
    public ResponseEntity<FrontendData> getTextByFrontId(@PathVariable Double frontId) {

        FrontendData frontendData = frontendDataRepository.findByFrontId(frontId);

        return new ResponseEntity<>(frontendData, HttpStatus.OK);
    }
}
