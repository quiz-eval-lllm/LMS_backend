package com.medis.repository;

import com.medis.model.course.CourseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseDB extends JpaRepository<CourseModel, String> {
    CourseModel findBySlugName(String slugName);

    @Query("SELECT c FROM CourseModel c WHERE c.isReleased = true AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%')))")
    List<CourseModel> findByReleasedTrue(String keyword, Pageable pageable);

    @Query("SELECT c FROM CourseModel c WHERE LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%'))")
    List<CourseModel> findByNameLike(String keyword, Pageable pageable);

    List<CourseModel> findByIsReleasedTrue();
}