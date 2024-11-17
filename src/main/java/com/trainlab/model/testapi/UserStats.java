package com.trainlab.model.testapi;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_stats", schema = "public")
public class UserStats { //для общей прожарки по специальностям у юзера
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private eUserLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialty")
    private eSpecialty specialty;

    @Column(name = "score")
    private  int score;

    @Column(name = "pre_score")
    private  int preScore;

    @Column(name =  "test_count")
    private  int testCount;
}


