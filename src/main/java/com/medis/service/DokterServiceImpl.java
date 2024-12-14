package com.medis.service;

import com.medis.model.user.DokterModel;
import com.medis.repository.DokterDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DokterServiceImpl implements DokterService {

    @Autowired
    DokterDB dokterDB;

    @Override
    public void addDokter(DokterModel dokter) {
        dokter.setPassword(encrypt(dokter.getPassword()));
        dokterDB.save(dokter);
    }

    @Override
    public boolean isEmailExist(String email) {
        if(dokterDB.findByEmail(email) == null) {
            return false;
        }
        return true;
    }

    @Override
    public DokterModel findByEmail(String email) {
        return dokterDB.findByEmail(email);
    }

    public static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }
}
