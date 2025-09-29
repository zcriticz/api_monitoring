package com.infinitytech.apimonitoring.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String errorDescription;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "ai_model_id")
    private AiModel aiModel;

    private String status;

    private String processId;

    private String extractedFields; 
}

//entidades para o banco de dados, onde serão mapeadas para uma tabela no PostgreSQL