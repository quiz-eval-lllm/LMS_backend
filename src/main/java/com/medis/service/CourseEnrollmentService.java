package com.medis.service;

import com.medis.model.course.*;
import com.medis.model.user.DokterModel;

import java.util.List;

public interface CourseEnrollmentService {

    List<CourseEnrollmentModel> getCourseEnrollmentByUser(DokterModel user, String keyword, int page);

    int getTotalEnrollment(DokterModel user, String keyword);

    CourseEnrollmentModel createEnrollment(CourseEnrollmentModel enrollment);

    SubCourseEnrollmentModel createSubCourseEnrollment(SubCourseEnrollmentModel enrollment);

    MaterialEnrollmentModel createMaterialEnrollment(MaterialEnrollmentModel enrollment);

    CourseEnrollmentModel updateEnrollment(CourseEnrollmentModel enrollment);

    SubCourseEnrollmentModel updateSubCourseEnrollment(SubCourseEnrollmentModel enrollment);

    MaterialEnrollmentModel updateMaterialEnrollment(MaterialEnrollmentModel enrollment);

    CourseEnrollmentModel getCourseEnrollment(DokterModel user, CourseModel course);

    List<CourseEnrollmentModel> getCourseParticipant(CourseModel course);

    SubCourseEnrollmentModel getSubCourseEnrollment(DokterModel user, SubCourseModel subcourse);

    MaterialEnrollmentModel getMaterialEnrollment(DokterModel user, MaterialModel material);
}
