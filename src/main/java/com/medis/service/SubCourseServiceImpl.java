package com.medis.service;

import com.medis.model.course.SubCourseModel;
import com.medis.repository.SubCourseDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCourseServiceImpl implements SubCourseService{
    @Autowired
    SubCourseDB subCourseDB;

    @Override
    public void addSubCourse(SubCourseModel subcourse) {
        subCourseDB.save(subcourse);
    }

    @Override
    public void updateSubCourse(SubCourseModel subcourse) {
        subCourseDB.save(subcourse);
    }

    @Override
    public void deleteSubCourse(SubCourseModel subCourse) {
        subCourseDB.delete(subCourse);
    }

    @Override
    public SubCourseModel getSubCourseByUuid(String uuid) {
        return subCourseDB.findById(uuid).orElse(null);
    }

}
