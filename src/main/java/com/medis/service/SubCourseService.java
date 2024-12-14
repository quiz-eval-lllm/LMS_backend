package com.medis.service;

import com.medis.model.course.SubCourseModel;

public interface SubCourseService {
    void addSubCourse(SubCourseModel subcourse);

    void updateSubCourse(SubCourseModel subcourse);

    void deleteSubCourse(SubCourseModel subCourse);

    SubCourseModel getSubCourseByUuid(String uuid);
}
