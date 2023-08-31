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
@TestPropertySource(properties = {"spring.profiles.active=test"})
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
                .username("testName1")
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        userRepository.saveAndFlush(user1);
        AuthenticationInfo authenticationInfo2 = AuthenticationInfo.builder()
                .email("test2@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user2 = User.builder()
                .username("testName2")
                .authenticationInfo(authenticationInfo2)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        userRepository.saveAndFlush(user2);
        AuthenticationInfo authenticationInfo3 = AuthenticationInfo.builder()
                .email("test3@gmail.com")
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user3 = User.builder()
                .username("testName3")
                .authenticationInfo(authenticationInfo3)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(true)
                .build();
        userRepository.saveAndFlush(user3);
    }

    @AfterAll
    void clearUsers() {
        userRepository.deleteAll();
    }

    @Test
    void findByAuthenticationInfoEmail() {
        String userEmail = "test1@gmail.com";
        Optional<User> userWithEmail = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userEmail);
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(userWithEmail.get().getAuthenticationInfo().getEmail())
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User expected = User.builder()
                .id(userWithEmail.get().getId())
                .username("testName1")
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        Optional<User> actual = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userEmail);
        assertEquals(expected, actual.orElse(null));
    }

    @Test
    void testFindAllByIsDeletedFalseOrderById_should_return_IsDeletedFalse() {
        String userEmail1 = "test1@gmail.com";
        String userEmail2 = "test2@gmail.com";
        Optional<User> userWithEmail1 = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userEmail1);
        Optional<User> userWithEmail2 = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userEmail2);
        AuthenticationInfo authenticationInfo1 = AuthenticationInfo.builder()
                .email(userWithEmail1.get().getAuthenticationInfo().getEmail())
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user1 = User.builder()
                .id(userWithEmail1.get().getId())
                .username("testName1")
                .authenticationInfo(authenticationInfo1)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        AuthenticationInfo authenticationInfo2 = AuthenticationInfo.builder()
                .email(userWithEmail2.get().getAuthenticationInfo().getEmail())
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User user2 = User.builder()
                .id(userWithEmail2.get().getId())
                .username("testName2")
                .authenticationInfo(authenticationInfo2)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        List<User> actual = userRepository.findAllByIsDeletedFalseOrderById();
        assertEquals(expected, actual);
    }

    @Test
    void findByIdAndIsDeletedFalse() {
        String userEmail = "test1@gmail.com";
        Optional<User> userWithEmail = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(userEmail);
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .email(userWithEmail.get().getAuthenticationInfo().getEmail())
                .userPassword("$2a$06$dnfljySL5wwO918hOHWkwOfOzSuQMORHXAr5em7CzMREoxnCJx2UC")
                .build();
        User expected = User.builder()
                .id(userWithEmail.get().getId())
                .username("testName1")
                .authenticationInfo(authenticationInfo)
                .created(Timestamp.valueOf("2023-07-13 16:28:08"))
                .changed(Timestamp.valueOf("2023-07-31 15:32:53"))
                .isActive(false)
                .isDeleted(false)
                .build();
        Optional<User> actual = userRepository.findByIdAndIsDeletedFalse(userWithEmail.get().getId());
        assertEquals(expected, actual.orElse(null));
    }
}