package com.medis.service;

import com.medis.model.course.CourseModel;
import com.medis.model.course.CourseTransactionModel;
import com.medis.model.user.DokterModel;
import com.medis.repository.CourseTransactionDB;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseTransactionServiceImpl implements CourseTransactionService {

    @Value("${midtrans.sandbox.client.key}")
    String CLIENT_KEY_MIDTRANS_SANDBOX;
    @Value("${midtrans.sandbox.server.key}")
    String SERVER_KEY_MIDTRANS_SANDBOX;

    @Value("${midtrans.prod.client.key}")
    String CLIENT_KEY_MIDTRANS_PROD;
    @Value("${midtrans.prod.server.key}")
    String SERVER_KEY_MIDTRANS_PROD;

    @Autowired
    private Environment env;

    @Autowired
    private CourseTransactionDB courseTransactionDB;

    @Override
    public String generateSnapToken(String orderID, long grossAmount, DokterModel user, CourseModel course) throws MidtransError {
        String activeProfile = env.getProperty("spring.profiles.active");
        if (activeProfile == null) activeProfile = env.getProperty("spring.profiles.default");
        boolean isProduction = activeProfile.equals("prod");

        if (isProduction) {
            Midtrans.serverKey = SERVER_KEY_MIDTRANS_PROD;
        } else {
            Midtrans.serverKey = SERVER_KEY_MIDTRANS_SANDBOX;
        }

        Midtrans.isProduction = isProduction;

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", orderID);
        transactionDetails.put("gross_amount", Integer.toString((int)grossAmount));

        Map<String, String> customerDetails = new HashMap<>();
        String[] nameSplit = user.getNama().split(" ");
        customerDetails.put("first_name", nameSplit[0]);
        customerDetails.put("last_name", nameSplit[nameSplit.length-1]);
        customerDetails.put("email", user.getEmail());

        Map<String, Object> params = new HashMap<>();
        params.put("transaction_details", transactionDetails);
        params.put("customer_details", customerDetails);

        return SnapApi.createTransactionToken(params);
    }

    @Override
    public CourseTransactionModel getActiveTransaction(DokterModel user, CourseModel course) {
        List<CourseTransactionModel> listTransaction = courseTransactionDB.findByUserAndCourse(user, course);
        for (CourseTransactionModel transaction : listTransaction) {
            if (transaction.getTransactionStatus().equals("temporary") || transaction.getTransactionStatus().equals("waiting for payment")) {
                return transaction;
            }
        }
        return null;
    }

    @Override
    public CourseTransactionModel getTransactionByOrderID(String orderID) {
        return courseTransactionDB.findByOrderId(orderID);
    }

    @Override
    public CourseTransactionModel createTransaction(CourseTransactionModel transaction) {
        return courseTransactionDB.save(transaction);
    }

    @Override
    public CourseTransactionModel updateTransaction(CourseTransactionModel transaction) {
        return courseTransactionDB.save(transaction);
    }

    @Override
    public String getClientKey(){
        String activeProfile = env.getProperty("spring.profiles.active");
        if (activeProfile == null) activeProfile = env.getProperty("spring.profiles.default");
        boolean isProduction = activeProfile.equals("prod");

        if (isProduction) return CLIENT_KEY_MIDTRANS_PROD;
        return CLIENT_KEY_MIDTRANS_SANDBOX;
    }
}
