package com.medis.service;


import com.medis.model.course.ReviewModel;
import com.medis.repository.ReviewDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    ReviewDB reviewDB;

    @Override
    public void addReview(ReviewModel review) {
        reviewDB.save(review);
    }
}
