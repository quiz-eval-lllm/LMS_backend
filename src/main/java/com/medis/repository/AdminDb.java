package com.medis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medis.model.user.AdminModel;

public interface AdminDb extends JpaRepository<AdminModel, String> {

}
