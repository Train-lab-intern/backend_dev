package com.trainlab.service.impl;

import com.trainlab.model.testapi.UserTestResult;
import com.trainlab.repository.UserTestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserTestResultServiceImpl {

   private final UserTestResultRepository userTestResultRepository;


}
