package iu.study.healthtraq.models;

import iu.study.healthtraq.utils.PolarActivity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "excercises")
public class HealthtraqExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "start_time")
    private ZonedDateTime startTime;
    @Column(name = "duration")
    private Duration duration;

    @Column(name = "distance")
    private Float distance;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "average_heartrate")
    private Integer averageHeartRate;

    @Column(name = "maximum_heartrate")
    private Integer maximumHeartRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private PolarActivity activity;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
}
