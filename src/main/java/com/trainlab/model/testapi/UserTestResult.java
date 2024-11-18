package com.trainlab.model.testapi;

import com.trainlab.Enum.eUserLevel;
import com.trainlab.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_test_result", schema = "public")
public class UserTestResult { //для статы после теста и графиков
    // мб надо будет кешировать для юзера каждого ...
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

    // сделать миграцию

    //дата
    @Column(name = "date")
    private LocalDate date;

    //прожарка
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private eUserLevel level;
}

//todo дтошки 2) для графиков
