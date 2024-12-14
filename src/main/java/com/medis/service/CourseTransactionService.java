package com.medis.service;

import com.medis.model.course.CourseModel;
import com.medis.model.course.CourseTransactionModel;
import com.medis.model.user.DokterModel;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.stereotype.Service;

public interface CourseTransactionService {

    String generateSnapToken(String orderID, long grossAmount, DokterModel user, CourseModel course) throws MidtransError;

    CourseTransactionModel getActiveTransaction(DokterModel user, CourseModel course);

    CourseTransactionModel getTransactionByOrderID(String orderID);

    CourseTransactionModel createTransaction(CourseTransactionModel transaction);

    CourseTransactionModel updateTransaction(CourseTransactionModel transaction);

    String getClientKey();
}
