package com.medis.repository;

import com.medis.model.course.CourseEnrollmentModel;
import com.medis.model.course.CourseModel;
import com.medis.model.user.DokterModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;

public interface CourseEnrollmentDB extends JpaRepository<CourseEnrollmentModel, String> {
    CourseEnrollmentModel findByUserAndCourse(DokterModel user, CourseModel course);

    List<CourseEnrollmentModel> findByCourse(CourseModel course);

    @Query("SELECT c FROM CourseEnrollmentModel c WHERE c.user = :user AND (:keyword IS NULL OR LOWER(c.course.name) LIKE LOWER(concat('%', :keyword, '%')))")
    List<CourseEnrollmentModel> findByUserLike(@Param("user") DokterModel user, @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT c FROM CourseEnrollmentModel c WHERE c.user = :user AND (:keyword IS NULL OR LOWER(c.course.name) LIKE LOWER(concat('%', :keyword, '%')))")
    List<CourseEnrollmentModel> findByUserLikeCount(@Param("user") DokterModel user, @Param("keyword") String keyword);
}
