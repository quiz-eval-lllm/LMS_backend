package com.medis.service;

import com.medis.model.user.UserModel;

public interface UserService {
    UserModel findByEmail(String email);
}
