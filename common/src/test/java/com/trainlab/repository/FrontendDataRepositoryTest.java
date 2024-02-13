//package com.trainlab.repository;
//
//import com.trainlab.TestApplication;
//import com.trainlab.model.FrontendData;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//@SpringJUnitConfig(TestApplication.class)
//@TestPropertySource(properties = {"spring.profiles.active=test"})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class FrontendDataRepositoryTest {
//    @Autowired
//    private FrontendDataRepository frontendDataRepository;
//
//    @BeforeAll
//    private void insertFrontendData() {
//        FrontendData frontendData1 = FrontendData.builder()
//                .id(1)
//                .frontId(1.1F)
//                .text("Создай свой успех")
//                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
//                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
//                .isDeleted(false)
//                .build();
//        frontendDataRepository.saveAndFlush(frontendData1);
//        FrontendData frontendData2 = FrontendData.builder()
//                .id(2)
//                .frontId(1.2F)
//                .text("Выполни задания")
//                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
//                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
//                .isDeleted(false)
//                .build();
//        frontendDataRepository.saveAndFlush(frontendData2);
//    }
//
//    @BeforeAll
//    private void clearFrontendData() {
//        frontendDataRepository.deleteAll();
//    }
//
//    @Test
//    void findDataByRange() {
//        int range = 1;
//        FrontendData frontendData1 = FrontendData.builder()
//                .id(1)
//                .frontId(1.1F)
//                .text("Создай свой успех")
//                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
//                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
//                .isDeleted(false)
//                .build();
//        FrontendData frontendData2 = FrontendData.builder()
//                .id(2)
//                .frontId(1.2F)
//                .text("Выполни задания")
//                .created(Timestamp.valueOf("2023-01-01 00:00:00"))
//                .changed(Timestamp.valueOf("2023-01-02 00:00:00"))
//                .isDeleted(false)
//                .build();
//        List<FrontendData> expected = new ArrayList<>();
//        expected.add(frontendData1);
//        expected.add(frontendData2);
//        List<FrontendData> actual = frontendDataRepository.findDataByRange(range);
//        assertEquals(expected, actual);
//    }
//}