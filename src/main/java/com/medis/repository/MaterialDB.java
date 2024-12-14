package com.medis.repository;

import com.medis.model.course.MaterialModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialDB extends JpaRepository<MaterialModel, String> {
    MaterialModel findByUuid(String uuid);
}