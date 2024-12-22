package com.medis.service;

import java.util.List;

import com.medis.model.user.UserModel;

public interface UserService {
    UserModel findByEmail(String email);

    UserModel findByNama(String nama);

    List<UserModel> findAll();
}
