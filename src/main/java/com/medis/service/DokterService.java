package com.medis.service;

import com.medis.model.user.DokterModel;

public interface DokterService {
    void addDokter(DokterModel dokter);

    boolean isEmailExist(String email);

    DokterModel findByEmail(String email);
}
