package com.trainlab.model.testapi;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.model.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test", schema = "public")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "test_questions",
            joinColumns = @JoinColumn(name = "test_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id")
    )
    private List<Question> questions;


    @Enumerated(EnumType.STRING)
    @Column(name = "specialty")
    private eSpecialty specialty;


    static private List<Answer> rightAnswers = new ArrayList<>();

    public  void  addRightAnswer(Answer answer){
        rightAnswers.add(answer);
    }

    public List<Answer> getRightAnswers(){
        return rightAnswers;
    }
}
