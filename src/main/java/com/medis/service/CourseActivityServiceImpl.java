package com.medis.service;

import com.medis.model.course.CourseActivityModel;
import com.medis.repository.CourseActivityDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseActivityServiceImpl implements CourseActivityService {

    @Autowired
    CourseActivityDB courseActivityDB;

    @Override
    public CourseActivityModel addActivity(CourseActivityModel activity) {
        return courseActivityDB.save(activity);
    }

    @Override
    public List<CourseActivityModel> getAllActivity() {
        return courseActivityDB.findAllByOrderByCreatedAtDesc();
    }
}
