package com.trainlab.repository;

import com.trainlab.TestApplication;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.Session;
import com.trainlab.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@SpringJUnitConfig(TestApplication.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
class SessionRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void findSessionByUserId() {
        Long userId = 1L;
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email("test@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx3UC")
                .build();
        User user = User.builder()
                .id(userId)
                .username("test")
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .active(false)
                .isDeleted(false)
                .build();
        Session session = Session.builder()
                .sessionId("1")
                .user(user)
                .sessionToken("8b65ed2b-3661-45d4-b3cf-24e2a38fbfc2")
                .created(Timestamp.valueOf("2023-07-31 20:14:25"))
                .changed(Timestamp.valueOf("2023-07-31 20:14:25"))
                .isDeleted(false)
                .build();
        userRepository.saveAndFlush(user);
        sessionRepository.saveAndFlush(session);
        List<Session> expected = new ArrayList<>();
        expected.add(session);
        List<Session> actual = sessionRepository.findSessionByUserId(userId);
        assertEquals(expected, actual);
    }
}