package com.infinitytech.apimonitoring.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "ai_models")
public class AiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelName; 
    private String version;   
}


