package iu.study.healthtraq.repositories;

import iu.study.healthtraq.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByPolarUserId(long polarUserId);
}
