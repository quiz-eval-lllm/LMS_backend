package com.medis.service;

import com.medis.model.course.*;
import com.medis.model.user.DokterModel;
import com.medis.repository.CourseEnrollmentDB;
import com.medis.repository.MaterialEnrollmentDB;
import com.medis.repository.SubCourseEnrollmentDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService{

    @Autowired
    CourseEnrollmentDB courseEnrollmentDB;

    @Autowired
    SubCourseEnrollmentDB subCourseEnrollmentDB;

    @Autowired
    MaterialEnrollmentDB materialEnrollmentDB;

    @Override
    public List<CourseEnrollmentModel> getCourseEnrollmentByUser(DokterModel user, String keyword, int page) {
        Pageable pageable = PageRequest.of(page-1, 8);
        return courseEnrollmentDB.findByUserLike(user, keyword, pageable);
    }

    @Override
    public int getTotalEnrollment(DokterModel user, String keyword) {
        int total = courseEnrollmentDB.findByUserLikeCount(user, keyword).size();
        return total;
    }

    @Override
    public CourseEnrollmentModel createEnrollment(CourseEnrollmentModel enrollment) {
        return courseEnrollmentDB.save(enrollment);
    }

    @Override
    public SubCourseEnrollmentModel createSubCourseEnrollment(SubCourseEnrollmentModel enrollment) {
        return subCourseEnrollmentDB.save(enrollment);
    }

    @Override
    public MaterialEnrollmentModel createMaterialEnrollment(MaterialEnrollmentModel enrollment) {
        return materialEnrollmentDB.save(enrollment);
    }

    @Override
    public CourseEnrollmentModel updateEnrollment(CourseEnrollmentModel enrollment) {
        return  courseEnrollmentDB.save(enrollment);
    }

    @Override
    public SubCourseEnrollmentModel updateSubCourseEnrollment(SubCourseEnrollmentModel enrollment) {
        return subCourseEnrollmentDB.save(enrollment);
    }

    @Override
    public MaterialEnrollmentModel updateMaterialEnrollment(MaterialEnrollmentModel enrollment) {
        return materialEnrollmentDB.save(enrollment);
    }

    @Override
    public CourseEnrollmentModel getCourseEnrollment(DokterModel user, CourseModel course) {
        CourseEnrollmentModel enrollment = courseEnrollmentDB.findByUserAndCourse(user, course);
        return enrollment;
    }

    @Override
    public List<CourseEnrollmentModel> getCourseParticipant(CourseModel course) {
        List<CourseEnrollmentModel> participant = courseEnrollmentDB.findByCourse(course);
        return participant;
    }

    @Override
    public SubCourseEnrollmentModel getSubCourseEnrollment(DokterModel user, SubCourseModel subcourse) {
        SubCourseEnrollmentModel enrollment = subCourseEnrollmentDB.findByUserAndSubcourse(user, subcourse);
        return enrollment;
    }

    @Override
    public MaterialEnrollmentModel getMaterialEnrollment(DokterModel user, MaterialModel material) {
        MaterialEnrollmentModel enrollment = materialEnrollmentDB.findByUserAndMaterial(user, material);
        return enrollment;
    }
}
