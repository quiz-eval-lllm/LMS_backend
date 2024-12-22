package com.medis.service;

import com.medis.model.user.UserModel;
import com.medis.repository.UserDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDB userDB;

    @Override
    public UserModel findByEmail(String email) {
        for (UserModel user : findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserModel findByNama(String nama) {
        Optional<UserModel> user = userDB.findByNama(nama);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

    @Override
    public List<UserModel> findAll() {
        return userDB.findAll();
    }

}
