package com.medis.service;

import com.medis.model.course.CourseModel;
import com.medis.repository.CourseDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseDB courseDB;

    @Override
    public CourseModel addCourse(CourseModel course) {
        return courseDB.save(course);
    }

    @Override
    public CourseModel updateCourse(CourseModel course) {
        return courseDB.save(course);
    }

    @Override
    public List<CourseModel> getAllCourse(int page) {
        Pageable pageable = PageRequest.of(page - 1, 8);
        return courseDB.findAll(pageable).getContent();
    }

    @Override
    public CourseModel getCourseBySlugName(String slugName) {
        return courseDB.findBySlugName(slugName);
    }

    @Override
    public CourseModel getCourseByUuid(String uuid) {
        return courseDB.findById(uuid).orElse(null);
    }

    @Override
    public void deleteCourse(CourseModel course) {
        courseDB.delete(course);
    }

    @Override
    public List<CourseModel> searchCourse(String keyword, int page) {
        Pageable pageable = PageRequest.of(page - 1, 8);
        return courseDB.findByNameLike(keyword, pageable);
    }

    @Override
    public List<CourseModel> searchReleasedCourse(String keyword, int page) {
        if (page == 0) {
            return courseDB.findByIsReleasedTrue();
        }
        Pageable pageable = PageRequest.of(page - 1, 8);
        return courseDB.findByReleasedTrue(keyword, pageable);
    }

    @Override
    public int getTotalCourse(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return (int) courseDB.count();
        }
        return courseDB.findByNameLike(keyword, Pageable.unpaged()).size();
    }

    @Override
    public int getTotalReleasedCourse(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return courseDB.findByIsReleasedTrue().size();
        }
        return courseDB.findByReleasedTrue(keyword, Pageable.unpaged()).size();
    }
}
