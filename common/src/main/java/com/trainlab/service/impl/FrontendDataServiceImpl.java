package com.trainlab.service.impl;

import com.trainlab.model.FrontendData;
import com.trainlab.repository.FrontendDataRepository;
import com.trainlab.service.FrontendDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FrontendDataServiceImpl implements FrontendDataService {
    private final FrontendDataRepository dataRepository;


    @Override
    public Map<String, String> getDataByPage(int page) {
        List<FrontendData> mainPageDataList = dataRepository.findDataByRange(page);

        return mainPageDataList.stream()
                .collect(Collectors.toMap(data -> Float.toString(data.getFrontId()), FrontendData::getText));
    }
}
