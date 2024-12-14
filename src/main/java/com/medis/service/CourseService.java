package com.medis.service;

import com.medis.model.course.CourseModel;

import java.util.List;
import java.util.Map;

public interface CourseService {
    CourseModel addCourse(CourseModel course);

    CourseModel updateCourse(CourseModel course);

    List<CourseModel> getAllCourse(int page);

    CourseModel getCourseBySlugName(String slugName);

    CourseModel getCourseByUuid(String uuid);

    void deleteCourse(CourseModel course);

    List<CourseModel> searchCourse(String keyword, int page);

    List<CourseModel> searchReleasedCourse(String keyword, int page);

    int getTotalCourse(String keyword);

    int getTotalReleasedCourse(String keyword);
}
