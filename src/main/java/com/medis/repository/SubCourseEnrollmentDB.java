package com.medis.repository;

import com.medis.model.course.SubCourseEnrollmentModel;
import com.medis.model.course.SubCourseModel;
import com.medis.model.user.DokterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCourseEnrollmentDB extends JpaRepository<SubCourseEnrollmentModel, String> {
    SubCourseEnrollmentModel findByUserAndSubcourse(DokterModel user, SubCourseModel subcourse);
}
