package com.medis.repository;

import com.medis.model.user.DokterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokterDB extends JpaRepository<DokterModel, String> {
    DokterModel findByEmail(String email);
}