package com.medis.service;

import com.medis.model.course.CourseActivityModel;

import java.util.List;

public interface CourseActivityService {
    CourseActivityModel addActivity(CourseActivityModel activity);

    List<CourseActivityModel> getAllActivity();
}
