package com.medis.controller;

import com.medis.dto.*;
import com.medis.model.course.*;
import com.medis.model.user.DokterModel;
import com.medis.security.JwtService;
import com.medis.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.midtrans.httpclient.error.MidtransError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private Environment env;

    @Autowired
    CourseService courseService;

    @Autowired
    JwtService jwtService;

    @Autowired
    DokterService dokterService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    MaterialService materialService;

    @Autowired
    CourseTransactionService courseTransactionService;

    @Autowired
    CourseEnrollmentService courseEnrollmentService;

    @Autowired
    CourseActivityService courseActivityService;

    @GetMapping("")
    public ResponseEntity<?> getAllCourses(@RequestParam(name = "keyword", required = false) String searchkeyword,
            @RequestParam(name = "page", defaultValue = "0") int searchpage) {
        CustomResponse response;
        List<CourseModel> listCourse = courseService.searchReleasedCourse(searchkeyword, searchpage);
        List<CourseResponse> data = new ArrayList<>();
        int total = courseService.getTotalReleasedCourse(searchkeyword);
        Map<String, Object> responseData = new HashMap<>();
        ModelMapper modelMapper = new ModelMapper();
        CourseResponse courseResponse;
        for (CourseModel course : listCourse) {
            courseResponse = modelMapper.map(course, CourseResponse.class);
            data.add(courseResponse);
        }
        responseData.put("courses", data);
        responseData.put("total", total);
        response = new CustomResponse(200, "Success", responseData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slugName}")
    public ResponseEntity<?> getCourse(@PathVariable("slugName") String slugName) {
        CustomResponse response;
        CourseModel course = courseService.getCourseBySlugName(slugName);
        if (course == null) {
            response = new CustomResponse(404, "Course not found", course);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ModelMapper modelMapper = new ModelMapper();
        FullCourseResponse data = modelMapper.map(course, FullCourseResponse.class);
        response = new CustomResponse(200, "Success", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("learn/{slugName}")
    public ResponseEntity<?> getDetailCourse(@PathVariable("slugName") String slugName) {
        CustomResponse response;
        CourseModel course = courseService.getCourseBySlugName(slugName);
        if (course == null) {
            response = new CustomResponse(404, "Course not found", course);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = new CustomResponse(200, "Success", course);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slugName}/{materialID}")
    public ResponseEntity<?> getMaterialID(@PathVariable("slugName") String slugName,
            @PathVariable("materialID") String materialID) {
        CustomResponse response;
        MaterialModel material = materialService.getMaterialByID(materialID);
        if (material == null) {
            response = new CustomResponse(404, "Material not found", material);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response = new CustomResponse(200, "Success", material);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/review/{slugName}")
    public ResponseEntity<?> reviewCourse(HttpServletRequest request, @PathVariable("slugName") String slugName,
            @Valid @RequestBody ReviewModel review, BindingResult bindingResult) throws Exception {
        CustomResponse response;
        if (bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel user = dokterService.findByEmail(email);

        CourseModel course = courseService.getCourseBySlugName(slugName);

        CourseEnrollmentModel enrollment = courseEnrollmentService.getCourseEnrollment(user, course);
        if (enrollment == null) {
            response = new CustomResponse(403, "You haven't enroll to this course", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else if (enrollment.isReviewed()) {
            response = new CustomResponse(500, "You have already reviewed this course", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        enrollment.setReviewed(true);
        courseEnrollmentService.updateEnrollment(enrollment);

        review.setName(user.getNama());
        review.setCourse(course);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        float totalRating = course.getRating() * course.getListReview().size();
        course.setRating((totalRating + review.getRating()) / (course.getListReview().size() + 1));
        Map<Integer, Integer> map = course.getRatingDistribution();
        int key = review.getRating();
        map.put(key, map.get(key) + 1);
        courseService.updateCourse(course);

        reviewService.addReview(review);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/enrolled")
    public ResponseEntity<?> getEnrolledCourse(HttpServletRequest request,
            @RequestParam(name = "keyword", required = false) String searchkeyword,
            @RequestParam(name = "page", defaultValue = "0") int searchpage) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // No token found in the header
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);
        if (dokter == null) {
            response = new CustomResponse(404, "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            List<CourseEnrollmentResponse> data = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            CourseEnrollmentResponse courseEnrollmentResponse;
            List<CourseEnrollmentModel> listEnrollment = courseEnrollmentService.getCourseEnrollmentByUser(dokter,
                    searchkeyword, searchpage);
            for (CourseEnrollmentModel course : listEnrollment) {
                courseEnrollmentResponse = modelMapper.map(course, CourseEnrollmentResponse.class);
                data.add(courseEnrollmentResponse);
            }
            int total = courseEnrollmentService.getTotalEnrollment(dokter, searchkeyword);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("enrollment", data);
            responseData.put("total", total);
            response = new CustomResponse(200, "Success", responseData);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/enrolled/{slugName}")
    public ResponseEntity<?> isEnrolled(HttpServletRequest request, @PathVariable("slugName") String slugName) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        CourseModel course = courseService.getCourseBySlugName(slugName);

        CourseEnrollmentModel enrollment = courseEnrollmentService.getCourseEnrollment(dokter, course);
        if (enrollment == null) {
            response = new CustomResponse(404, "Enrollment not found", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            boolean isEnrolled = true;
            boolean isReviewed = enrollment.isReviewed();
            Map<String, Boolean> data = new HashMap<String, Boolean>();
            data.put("isEnrolled", isEnrolled);
            data.put("isReviewed", isReviewed);
            response = new CustomResponse(200, "Success", data);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @GetMapping("/buy/{slugName}")
    public ResponseEntity<?> buyCourse(HttpServletRequest request, @PathVariable("slugName") String slugName) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        CourseModel course = courseService.getCourseBySlugName(slugName);

        if (course.getPrice() == 0) {
            CourseEnrollmentModel courseEnrollment = new CourseEnrollmentModel();
            courseEnrollment.setReviewed(false);
            courseEnrollment.setUser(dokter);
            courseEnrollment.setCourse(course);
            courseEnrollment.setFinishedMaterial(0);
            courseEnrollment.setCreatedAt(LocalDateTime.now());
            courseEnrollmentService.createEnrollment(courseEnrollment);
            for (SubCourseModel subcourse : course.getListSubCourse()) {
                SubCourseEnrollmentModel subCourseEnrollment = new SubCourseEnrollmentModel();
                subCourseEnrollment.setUser(dokter);
                subCourseEnrollment.setCourse(course);
                subCourseEnrollment.setFinishedMaterial(0);
                subCourseEnrollment.setCreatedAt(LocalDateTime.now());
                subCourseEnrollment.setSubcourse(subcourse);
                courseEnrollmentService.createSubCourseEnrollment(subCourseEnrollment);
                for (MaterialModel material : subcourse.getListMaterial()) {
                    MaterialEnrollmentModel materialEnrollment = new MaterialEnrollmentModel();
                    materialEnrollment.setUser(dokter);
                    materialEnrollment.setCourse(course);
                    materialEnrollment.setSubcourse(subcourse);
                    materialEnrollment.setMaterial(material);
                    materialEnrollment.setCreatedAt(LocalDateTime.now());
                    materialEnrollment.setFinished(false);
                    courseEnrollmentService.createMaterialEnrollment(materialEnrollment);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        CourseTransactionModel transaction = courseTransactionService.getActiveTransaction(dokter, course);

        String activeProfile = env.getProperty("spring.profiles.active");
        if (activeProfile == null)
            activeProfile = env.getProperty("spring.profiles.default");
        boolean isProduction = activeProfile.equals("prod");

        String snapToken;
        if (transaction == null) {
            String orderId = UUID.randomUUID().toString();
            LocalDateTime tokenExpireDate = LocalDateTime.now().plusDays(1);
            try {
                snapToken = courseTransactionService.generateSnapToken(orderId, course.getPrice(), dokter, course);
            } catch (MidtransError e) {
                response = new CustomResponse(500, "Error to generate token", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            transaction = new CourseTransactionModel();
            transaction.setOrderId(orderId);
            transaction.setSnapToken(snapToken);
            transaction.setTransactionStatus("temporary");
            transaction.setCourse(course);
            transaction.setUser(dokter);
            transaction.setTokenExpireDate(tokenExpireDate);
            courseTransactionService.createTransaction(transaction);
        } else if (LocalDateTime.now().isAfter(transaction.getTokenExpireDate())) {
            String orderId = UUID.randomUUID().toString();
            LocalDateTime tokenExpireDate = LocalDateTime.now().plusDays(1);
            try {
                snapToken = courseTransactionService.generateSnapToken(orderId, course.getPrice(), dokter, course);
            } catch (MidtransError e) {
                response = new CustomResponse(500, "Error to generate token", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            transaction.setOrderId(orderId);
            transaction.setSnapToken(snapToken);
            transaction.setTokenExpireDate(tokenExpireDate);
            courseTransactionService.updateTransaction(transaction);
        }

        snapToken = transaction.getSnapToken();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("client_key", courseTransactionService.getClientKey());
        data.put("snap_token", snapToken);
        data.put("transaction", transaction);
        data.put("is_production", isProduction);
        response = new CustomResponse(200, "Success", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/enroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> enrollCourse(@RequestBody Map<String, Object> payload) {
        if (!(payload.isEmpty())) {
            String orderID = (String) payload.get("order_id");
            String transactionStatus = (String) payload.get("transaction_status");
            String transactionTime = (String) payload.get("transaction_time");
            String paymentType = (String) payload.get("payment_type");
            String fraudStatus = (String) payload.get("fraud_status");

            CourseTransactionModel transaction = courseTransactionService.getTransactionByOrderID(orderID);
            CourseModel course = transaction.getCourse();
            DokterModel user = transaction.getUser();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            if (fraudStatus.equals("deny")) {
                transaction.setTransactionStatus("fraud");
                courseTransactionService.updateTransaction(transaction);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fraud detected");
            }

            if (transactionStatus.equals("settlement") || transactionStatus.equals("capture")) {
                course.setTotalParticipant(course.getTotalParticipant() + 1);
                courseService.updateCourse(course);
                // create enrollment
                CourseEnrollmentModel courseEnrollment = new CourseEnrollmentModel();
                courseEnrollment.setReviewed(false);
                courseEnrollment.setUser(user);
                courseEnrollment.setCourse(course);
                courseEnrollment.setFinishedMaterial(0);
                courseEnrollment.setCreatedAt(LocalDateTime.parse(transactionTime, formatter));
                courseEnrollmentService.createEnrollment(courseEnrollment);
                for (SubCourseModel subcourse : course.getListSubCourse()) {
                    SubCourseEnrollmentModel subCourseEnrollment = new SubCourseEnrollmentModel();
                    subCourseEnrollment.setUser(user);
                    subCourseEnrollment.setCourse(course);
                    subCourseEnrollment.setFinishedMaterial(0);
                    subCourseEnrollment.setCreatedAt(LocalDateTime.parse(transactionTime, formatter));
                    subCourseEnrollment.setSubcourse(subcourse);
                    courseEnrollmentService.createSubCourseEnrollment(subCourseEnrollment);
                    for (MaterialModel material : subcourse.getListMaterial()) {
                        MaterialEnrollmentModel materialEnrollment = new MaterialEnrollmentModel();
                        materialEnrollment.setUser(user);
                        materialEnrollment.setCourse(course);
                        materialEnrollment.setSubcourse(subcourse);
                        materialEnrollment.setMaterial(material);
                        materialEnrollment.setCreatedAt(LocalDateTime.parse(transactionTime, formatter));
                        materialEnrollment.setFinished(false);
                        courseEnrollmentService.createMaterialEnrollment(materialEnrollment);
                    }
                }
                String settlementTime = (String) payload.get("settlement_time");
                transaction.setSettlementTime(LocalDateTime.parse(settlementTime, formatter));
                transaction.setTransactionStatus("paid");
            } else if (transactionStatus.equals("pending")) {
                transaction.setTransactionTime(LocalDateTime.parse(transactionTime, formatter));
                transaction.setPaymentType(paymentType);
                transaction.setTransactionStatus("waiting for payment");
            } else if (transactionStatus.equals("expire") || transactionStatus.equals("deny")
                    || transactionStatus.equals("cancel")) {
                transaction.setTransactionStatus("failed");
            }
            courseTransactionService.updateTransaction(transaction);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/tick/{materialID}")
    public ResponseEntity<?> tickMaterialProgress(HttpServletRequest request,
            @PathVariable("materialID") String materialID, @RequestBody Map<String, Boolean> req) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        MaterialModel material = materialService.getMaterialByID(materialID);

        MaterialEnrollmentModel materialEnrollment = courseEnrollmentService.getMaterialEnrollment(dokter, material);
        SubCourseEnrollmentModel subcourseEnrollment = courseEnrollmentService.getSubCourseEnrollment(dokter,
                materialEnrollment.getSubcourse());
        CourseEnrollmentModel courseEnrollment = courseEnrollmentService.getCourseEnrollment(dokter,
                materialEnrollment.getCourse());
        if (req.get("is_finished")) {
            if (!materialEnrollment.isFinished()) {
                materialEnrollment.setFinished(true);
                subcourseEnrollment.setFinishedMaterial(subcourseEnrollment.getFinishedMaterial() + 1);
                courseEnrollment.setFinishedMaterial(courseEnrollment.getFinishedMaterial() + 1);
            }
        } else {
            if (materialEnrollment.isFinished()) {
                materialEnrollment.setFinished(false);
                subcourseEnrollment.setFinishedMaterial(subcourseEnrollment.getFinishedMaterial() - 1);
                courseEnrollment.setFinishedMaterial(courseEnrollment.getFinishedMaterial() - 1);
            }
        }
        courseEnrollmentService.updateMaterialEnrollment(materialEnrollment);
        courseEnrollmentService.updateSubCourseEnrollment(subcourseEnrollment);
        courseEnrollmentService.updateEnrollment(courseEnrollment);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/material/progress/{materialID}")
    public ResponseEntity<?> getMaterialProgress(HttpServletRequest request,
            @PathVariable("materialID") String materialID) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        MaterialModel material = materialService.getMaterialByID(materialID);
        MaterialEnrollmentModel materialEnrollment = courseEnrollmentService.getMaterialEnrollment(dokter, material);
        Map<String, Boolean> data = new HashMap<>();
        data.put("is_finished", materialEnrollment.isFinished());
        response = new CustomResponse(200, "Success", data);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/history-transaction")
    public ResponseEntity<?> getHistoryTransaction(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int searchpage,
            @RequestParam(name = "month", required = false) Integer searchmonth,
            @RequestParam(name = "year", required = false) Integer searchyear) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        List<CourseTransactionModel> filteredTransactions = new ArrayList<>(dokter.getListTransaction());
        if (searchmonth != null && searchyear != null) {
            filteredTransactions = filteredTransactions.stream()
                    .filter(transaction -> {
                        int transactionMonth = transaction.getTransactionTime().getMonthValue();
                        int transactionYear = transaction.getTransactionTime().getYear();
                        return transactionMonth == searchmonth && transactionYear == searchyear;
                    })
                    .collect(Collectors.toList());
        }

        List<TransactionResponse> listTransactions = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        TransactionResponse transactionResponse;
        for (CourseTransactionModel transaction : filteredTransactions) {
            transactionResponse = modelMapper.map(transaction, TransactionResponse.class);
            transactionResponse.setCourseName(transaction.getCourse().getName());
            transactionResponse.setCoursePrice(transaction.getCourse().getPrice());
            listTransactions.add(transactionResponse);
        }

        int total = listTransactions.size();
        int start = (searchpage - 1) * 10;
        int end = Math.min(start + 10, total);

        List<TransactionResponse> data;
        if (start < total) {
            data = listTransactions.subList(start, end);
        } else {
            data = null;
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("transactions", data);
        responseData.put("total", total);

        response = new CustomResponse(200, "Success", responseData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/log-activity")
    public ResponseEntity<?> getLogActivity(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "1") int searchpage,
            @RequestParam(name = "course", required = false) String course) {
        CustomResponse response;
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        String email = jwtService.extractEmail(token);
        DokterModel dokter = dokterService.findByEmail(email);

        // Filter activities by course name if searchCourse is provided
        List<CourseActivityModel> filteredActivities = new ArrayList<>(dokter.getListActivty());
        if (course != null && !course.isEmpty()) {
            filteredActivities = filteredActivities.stream()
                    .filter(activity -> activity.getCourse().toLowerCase().contains(course.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int total = filteredActivities.size();
        int start = (searchpage - 1) * 10;
        int end = Math.min(start + 10, total);

        filteredActivities.sort(Comparator.comparing(CourseActivityModel::getCreatedAt).reversed());

        List<CourseActivityModel> data;
        if (start < total) {
            data = filteredActivities.subList(start, end);
        } else {
            data = null;
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("activities", data);
        responseData.put("total", total);

        response = new CustomResponse(200, "Success", responseData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/log-activity")
    public ResponseEntity<?> postLogActivity(HttpServletRequest request,
            @Valid @RequestBody CourseActivityModel activity, BindingResult bindingResult) {
        CustomResponse response;
        if (bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            response = new CustomResponse(403, "Invalid token", null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // No token found in the header
        }

        String email = jwtService.extractEmail(token);
        DokterModel user = dokterService.findByEmail(email);
        activity.setUser(user);

        activity.setCreatedAt(LocalDateTime.now());
        courseActivityService.addActivity(activity);

        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }
}
