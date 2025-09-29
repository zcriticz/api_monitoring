package com.infinitytech.apimonitoring.repository;

import com.infinitytech.apimonitoring.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}
