package com.medis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.medis.model.user.AdminModel;
import com.medis.model.user.UserModel;
import com.medis.repository.AdminDb;
import com.medis.repository.UserDB;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDb adminDb;

    @Override
    public void addAdmin(AdminModel admin) {
        admin.setPassword(encrypt(admin.getPassword()));
        adminDb.save(admin);
    }

    public static String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

}
