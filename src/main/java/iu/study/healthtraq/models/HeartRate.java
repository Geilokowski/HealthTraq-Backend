package iu.study.healthtraq.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "heartrates")
public class HeartRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    

    @Column(name = "time")
    Date time;

}
