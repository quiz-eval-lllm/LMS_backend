package com.medis.service;

import com.medis.model.course.MaterialModel;
import com.medis.repository.MaterialDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialDB materialDB;

    @Override
    public MaterialModel getMaterialByID(String uuid) {
        return materialDB.findByUuid(uuid);
    }

    @Override
    public void addMaterial(MaterialModel material) {
        materialDB.save(material);
    }

    @Override
    public void updateMaterial(MaterialModel material) {
        materialDB.save(material);
    }

    @Override
    public void deleteMaterial(MaterialModel material) {
        materialDB.delete(material);
    }
}
