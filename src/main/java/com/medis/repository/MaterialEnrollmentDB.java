package com.medis.repository;

import com.medis.model.course.MaterialEnrollmentModel;
import com.medis.model.course.MaterialModel;
import com.medis.model.user.DokterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialEnrollmentDB extends JpaRepository<MaterialEnrollmentModel, String> {
    MaterialEnrollmentModel findByUserAndMaterial(DokterModel user, MaterialModel material);
}
