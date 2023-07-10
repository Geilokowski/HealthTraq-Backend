package iu.study.healthtraq.repositories;

import iu.study.healthtraq.models.HealthtraqExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<HealthtraqExercise, Long> {
    boolean existsBySourceId(String sourceId);
}
