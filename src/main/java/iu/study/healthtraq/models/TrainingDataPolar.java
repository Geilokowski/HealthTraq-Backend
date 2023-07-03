package iu.study.healthtraq.models;

import java.util.Date;

public class TrainingDataPolar {

    int exerciseId;
    Date upload_time;
    int polarUser;
    int transaction_id;
    String device;
    int device_id;
    Date start_time;

    //duration machen wir wie?

    float calories;
    float distance;
    float training_load;
    String sport;
    boolean has_route;
    int club_id;
    String club_name;
    String detailed_sport_info;
    float fat_percentage;
    float carbohydrate_percentage;
    float protein_percentage;
    float running_index;

    //aus den Trainingsdaten training-load pro Sachen auch?
}
