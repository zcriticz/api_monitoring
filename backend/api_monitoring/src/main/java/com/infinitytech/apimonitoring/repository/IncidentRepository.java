package com.infinitytech.apimonitoring.repository;

import com.infinitytech.apimonitoring.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Optional<Incident> findByProcessId(String processId);
    List<Incident> findByClientId(Long clientId);
    List<Incident> findByAiModelId(Long aiModelId);
    List<Incident> findByAiModelIdAndTimestampBetween(Long aiModelId, LocalDateTime start, LocalDateTime end);
    List<Incident> findByClientIdAndTimestampBetween(Long clientId, LocalDateTime start, LocalDateTime end);
    List<Incident> findByClientIdAndAiModelIdAndTimestampBetween(Long clientId, Long aiModelId, LocalDateTime start, LocalDateTime end);
    List<Incident> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<Incident> findByErrorDescriptionIsNotNull();

    List<Incident> findAll();
}