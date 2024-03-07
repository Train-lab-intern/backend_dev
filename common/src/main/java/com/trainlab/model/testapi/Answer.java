package com.trainlab.model.testapi;

import com.trainlab.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answer", schema = "public")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "answer_txt")
    private String answerTxt;

    @Column(name = "answer_num")
    private int answerNum;

    @Column(name = "is_correct")
    private boolean isCorrect;

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
}

