package com.medis.repository;

import com.medis.model.course.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDB extends JpaRepository<ReviewModel, String> {
}
