package com.medis.service;

import com.medis.model.user.UserModel;
import com.medis.repository.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserDB userDB;

    @Override
    public UserModel findByEmail(String email) {
        Optional<UserModel> user = userDB.findByEmail(email);
        if(user!=null) {
            return user.get();
        }
        return null;
    }

}
