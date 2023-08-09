package com.trainlab.repository;

import com.trainlab.TestApplication;
import com.trainlab.model.AuthenticationInfo;
import com.trainlab.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@SpringJUnitConfig(TestApplication.class)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop", "spring.profiles.active=test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void insertUsers() {
        AuthenticationInfo authenticationInfo1 = AuthenticationInfo.builder()
                .email("test1@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user1 = User.builder()
                .id(1L)
                .username("testName1")
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf("2023-07-13 16:28:08.000000"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53.462876"))
                .active(false)
                .isDeleted(false)
                .build();
        userRepository.save(user1);
        AuthenticationInfo authenticationInfo2 = AuthenticationInfo.builder()
                .email("test2@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("testName2")
                .authenticationInfo(authenticationInfo2)
                .created(Timestamp.valueOf("2023-07-13 16:28:08.000000"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53.462876"))
                .active(false)
                .isDeleted(false)
                .build();
        userRepository.save(user2);
    }

    @AfterAll
    void clearUsers() {
        userRepository.deleteAll();
    }

    @Test
    void findByAuthenticationInfoEmail() {
        String userEmail = "test1@gmail.com";
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email("test1@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User expected = User.builder()
                .id(1L)
                .username("testName1")
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-07-13 16:28:08.000000"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53.462876"))
                .active(false)
                .isDeleted(false)
                .build();
        Optional<User> actual = userRepository.findByAuthenticationInfoEmail(userEmail);
        assertEquals(expected, actual.orElse(null));
    }

    @Test
    void testFindAllByIsDeletedFalseOrderById() {
        AuthenticationInfo authenticationInfo1 = AuthenticationInfo.builder()
                .email("test1@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user1 = User.builder()
                .id(1L)
                .username("testName1")
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf("2023-07-13 16:28:08.000000"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53.462876"))
                .active(false)
                .isDeleted(false)
                .build();
        userRepository.save(user1);
        AuthenticationInfo authenticationInfo2 = AuthenticationInfo.builder()
                .email("test2@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("testName2")
                .authenticationInfo(authenticationInfo2)
                .created(Timestamp.valueOf("2023-07-13 16:28:08.000000"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53.462876"))
                .active(false)
                .isDeleted(false)
                .build();
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        List<User> actual = userRepository.findAllByIsDeletedFalseOrderById();
        assertEquals(expected, actual);
    }
}