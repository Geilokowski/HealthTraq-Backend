package iu.study.healthtraq.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "fetching_data")
    private boolean fetchingData;

    @Column(name = "polar_user_id")
    private int polarUserId;
    @Column(name = "polar_member_id")
    private String polarMemberId;
    @Column(name = "polar_token")
    private String polarToken;

    @OneToMany(mappedBy = "participant")
    private List<HealthtraqExercise> exercises;
}
