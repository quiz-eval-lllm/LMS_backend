package com.medis.service;

import com.medis.model.course.MaterialModel;

public interface MaterialService {
    MaterialModel getMaterialByID(String uuid);

    void addMaterial(MaterialModel material);

    void updateMaterial(MaterialModel material);

    void deleteMaterial(MaterialModel material);
}
