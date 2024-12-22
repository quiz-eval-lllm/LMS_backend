package com.medis.repository;

import com.medis.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDB extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUuid(String uuid);

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByNama(String nama);

    // Optional<UserModel> findByForgotUrl(String forgotUrl);
    // List<UserModel> findByBannedStatusAndRoleAndEmailContainingIgnoreCase(String
    // status, String role, String search);
    //
    // List<UserModel> findByRoleAndEmailContainingIgnoreCase(String role, String
    // search);
    //
    // List<UserModel> findByEmailContainingIgnoreCase(String search);
    //
    // List<UserModel> findByBannedStatusAndEmailContainingIgnoreCase(String status,
    // String search);

}
