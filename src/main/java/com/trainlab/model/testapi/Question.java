package com.trainlab.model.testapi;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question", schema = "public")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "question_txt")
    private String questionTxt;

    @Column(name = "question_num")
    private int questionNum;


    @OneToMany(mappedBy = "question",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Answer> answers;
}
