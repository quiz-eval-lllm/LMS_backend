package com.medis.repository;

import com.medis.model.course.CourseModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDB extends JpaRepository<CourseModel, String> {
    CourseModel findBySlugName(String slugName);

    // Use @Param to bind the :keyword parameter
    @Query("SELECT c FROM CourseModel c WHERE c.isReleased = true AND (:keyword IS NULL OR LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%')))")
    List<CourseModel> findByReleasedTrue(@Param("keyword") String keyword, Pageable pageable);

    // Use @Param for :keyword here as well
    @Query("SELECT c FROM CourseModel c WHERE LOWER(c.name) LIKE LOWER(concat('%', :keyword, '%'))")
    List<CourseModel> findByNameLike(@Param("keyword") String keyword, Pageable pageable);

    // This method doesn't require any changes as it uses derived query methods
    List<CourseModel> findByIsReleasedTrue();
}