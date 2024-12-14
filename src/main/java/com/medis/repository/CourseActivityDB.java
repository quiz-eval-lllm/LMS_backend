package com.medis.repository;

import com.medis.model.course.CourseActivityModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseActivityDB extends JpaRepository<CourseActivityModel, String> {
    List<CourseActivityModel> findAllByOrderByCreatedAtDesc();
}