package com.medis.repository;

import com.medis.model.course.DiscussionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionDB extends JpaRepository<DiscussionModel, String> {
    DiscussionModel findByUuid(String uuid);
}