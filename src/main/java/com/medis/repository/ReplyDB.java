package com.medis.repository;

import com.medis.model.course.ReplyModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyDB extends JpaRepository<ReplyModel, String> {
    ReplyModel findByUuid(String uuid);
}
