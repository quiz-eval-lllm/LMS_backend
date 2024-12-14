package com.medis.repository;

import com.medis.model.course.CourseModel;
import com.medis.model.course.CourseTransactionModel;
import com.medis.model.user.DokterModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTransactionDB extends JpaRepository<CourseTransactionModel, String> {
    List<CourseTransactionModel> findByUserAndCourse(DokterModel user, CourseModel course);

    CourseTransactionModel findByOrderId(String orderId);
}
