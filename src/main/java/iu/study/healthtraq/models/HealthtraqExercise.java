package iu.study.healthtraq.models;

import iu.study.healthtraq.utils.PolarActivity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "distance")
    private Long distance;
    @Column(name = "calories")
    private Long calories;

    @Column(name = "source_id")
    private String sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private PolarActivity activity;
}
