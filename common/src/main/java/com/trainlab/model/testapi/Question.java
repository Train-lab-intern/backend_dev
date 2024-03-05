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
    String questionTxt;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "question_answers",
            joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id", referencedColumnName = "id")
    )
    private List<Answer> answers;
}
