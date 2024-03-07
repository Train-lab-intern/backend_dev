package com.trainlab.model.testapi;

import com.trainlab.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_test_result", schema = "public")
public class UserTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @Column(name = "score")
    private  int score;

    @Column(name = "complete_time")
    private Long completeTime;
}
