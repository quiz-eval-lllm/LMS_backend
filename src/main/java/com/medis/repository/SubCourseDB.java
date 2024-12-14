package com.medis.repository;

import com.medis.model.course.SubCourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCourseDB extends JpaRepository<SubCourseModel, String> {
}
