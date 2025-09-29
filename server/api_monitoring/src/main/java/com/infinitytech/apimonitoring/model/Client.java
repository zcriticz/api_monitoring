package com.infinitytech.apimonitoring.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactInfo; 
}
