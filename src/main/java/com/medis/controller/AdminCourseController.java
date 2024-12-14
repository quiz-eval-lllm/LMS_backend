package com.medis.controller;

import com.medis.dto.*;
import com.medis.model.course.*;
import com.medis.service.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v1/admin")
public class AdminCourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    SubCourseService subCourseService;

    @Autowired
    MaterialService materialService;

    @Autowired
    DiscussionService discussionService;

    @Autowired
    ReplyService replyService;

    @Autowired
    CourseActivityService courseActivityService;

    @Autowired
    CourseEnrollmentService courseEnrollmentService;

    @GetMapping("/course")
    public ResponseEntity<?> getAllCourses(@RequestParam(required = false) String keyword, @RequestParam int page) {
        CustomResponse response;
        List<CourseModel> listCourse;
        if(keyword!=null) {
            listCourse = courseService.searchCourse(keyword, page);
        } else {
            listCourse = courseService.getAllCourse(page);
        }
        List<CourseResponse> data = new ArrayList<>();
        int total = courseService.getTotalCourse(keyword);
        Map<String, Object> responseData = new HashMap<>();
        ModelMapper modelMapper = new ModelMapper();
        CourseResponse courseResponse;
        for(CourseModel course:listCourse) {
            courseResponse = modelMapper.map(course, CourseResponse.class);
            data.add(courseResponse);
        }
        responseData.put("courses", data);
        responseData.put("total", total);
        response = new CustomResponse(200, "Success", responseData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/unique/{name}")
    public ResponseEntity<?> isCourseExist(@PathVariable("name") String name) {
        CustomResponse response;
        String slugName = name.toLowerCase().replace(" ", "-");
        CourseModel existingCourse = courseService.getCourseBySlugName(slugName);
        if(existingCourse != null) {
            response = new CustomResponse(400, "Course name must be unique", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response = new CustomResponse(200, "Success", true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/course/create")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRequest course, BindingResult bindingResult) {
        CustomResponse response;
        if(bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }
        String slugName = course.getName().toLowerCase().replace(" ", "-");
        CourseModel existingCourse = courseService.getCourseBySlugName(slugName);
        if(existingCourse != null) {
            response = new CustomResponse(400, "Course name must be unique", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ModelMapper modelMapper = new ModelMapper();
        CourseModel newCourse = modelMapper.map(course, CourseModel.class);
        newCourse.setSlugName(slugName);
        newCourse.setTotalParticipant(0);
        newCourse.setReleased(false);
        newCourse.setCreatedAt(LocalDateTime.now());
        newCourse.setUpdatedAt(LocalDateTime.now());
        int totalMaterial = 0;
        for (SubCourseRequest subcourse : course.getSubcourses()) {
            totalMaterial+=subcourse.getMaterials().size();
        }
        newCourse.setTotalMaterial(totalMaterial);
        newCourse.setRating(0);
        Map<Integer, Integer> ratingDistribution = new HashMap<>() {{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
        }};
        newCourse.setRatingDistribution(ratingDistribution);
        courseService.addCourse(newCourse);
        for (SubCourseRequest subcourse : course.getSubcourses()) {
            SubCourseModel newSubCourse = modelMapper.map(subcourse, SubCourseModel.class);
            newSubCourse.setCreatedAt(LocalDateTime.now());
            newSubCourse.setUpdatedAt(LocalDateTime.now());
            newSubCourse.setCourse(newCourse);
            subCourseService.addSubCourse(newSubCourse);
            for (MaterialRequest material : subcourse.getMaterials()) {
                MaterialModel newMaterial = modelMapper.map(material, MaterialModel.class);
                newMaterial.setCreatedAt(LocalDateTime.now());
                newMaterial.setUpdatedAt(LocalDateTime.now());
                newMaterial.setSubCourse(newSubCourse);
                materialService.addMaterial(newMaterial);
            }
        }
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/course/update/{uuid}")
    public ResponseEntity<?> updateCourse(@PathVariable("uuid") String uuid, @Valid @RequestBody CourseRequest course, BindingResult bindingResult) {
        CustomResponse response;
        if(bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }
        String slugName = course.getName().toLowerCase().replace(" ", "-");
        CourseModel existingCourse = courseService.getCourseByUuid(uuid);
        CourseModel anotherExistingCourse = courseService.getCourseBySlugName(slugName);
        if(anotherExistingCourse != null && anotherExistingCourse.getUuid() != uuid) {
            response = new CustomResponse(400, "Course name must be unique", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ModelMapper modelMapper = new ModelMapper();
        existingCourse.setName(course.getName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setPrice(course.getPrice());
        existingCourse.setImage_url(course.getImage_url());
        existingCourse.setImage_token(course.getImage_token());
        existingCourse.setSlugName(slugName);
        existingCourse.setTotalParticipant(0);
        existingCourse.setExamLink(course.getExamLink());
        existingCourse.setReleased(false);
        existingCourse.setUpdatedAt(LocalDateTime.now());
        int totalMaterial = 0;
        for (SubCourseRequest subcourse : course.getSubcourses()) {
            totalMaterial+=subcourse.getMaterials().size();
        }
        existingCourse.setTotalMaterial(totalMaterial);
        existingCourse.setRating(0);
        Map<Integer, Integer> ratingDistribution = new HashMap<>() {{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
        }};
        existingCourse.setRatingDistribution(ratingDistribution);
        courseService.updateCourse(existingCourse);
        for (SubCourseRequest subcourse : course.getSubcourses()) {
            SubCourseModel existingSubCourse;
            if(subcourse.getUuid() == null) {
                existingSubCourse = modelMapper.map(subcourse, SubCourseModel.class);
                existingSubCourse.setCreatedAt(LocalDateTime.now());
            } else {
                existingSubCourse = subCourseService.getSubCourseByUuid(subcourse.getUuid());
                existingSubCourse.setName(subcourse.getName());
                existingSubCourse.setDescription(subcourse.getDescription());
            }
            existingSubCourse.setUpdatedAt(LocalDateTime.now());
            existingSubCourse.setCourse(existingCourse);
            subCourseService.updateSubCourse(existingSubCourse);
            for (MaterialRequest material : subcourse.getMaterials()) {
                MaterialModel existingMaterial;
                if(material.getUuid() == null) {
                    existingMaterial = modelMapper.map(material, MaterialModel.class);
                    existingMaterial.setCreatedAt(LocalDateTime.now());
                } else {
                    existingMaterial = materialService.getMaterialByID(material.getUuid());
                    existingMaterial.setName(material.getName());
                    existingMaterial.setDescription(material.getDescription());
                    existingMaterial.setType(material.getType());
                    existingMaterial.setMaterial_url(material.getMaterial_url());
                    existingMaterial.setToken(material.getToken());
                    existingMaterial.setReadingMinute(material.getReadingMinute());
                }
                existingMaterial.setUpdatedAt(LocalDateTime.now());
                existingMaterial.setSubCourse(existingSubCourse);
                materialService.updateMaterial(existingMaterial);
            }
        }
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/release/{slugName}")
    public ResponseEntity<?> releaseCourse(@PathVariable("slugName") String slugName) {
        CustomResponse response;
        CourseModel course = courseService.getCourseBySlugName(slugName);
        if(course == null) {
            response = new CustomResponse(404, "Course not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        course.setReleased(true);
        courseService.updateCourse(course);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/participant/{slugName}")
    public ResponseEntity<?> getCourseParticipant(@PathVariable("slugName") String slugName, @RequestParam(name = "name", required = false) String searchName, @RequestParam(defaultValue = "1") int page) {
        CustomResponse response;
        CourseModel course = courseService.getCourseBySlugName(slugName);
        List<CourseEnrollmentModel> listEnrollment = courseEnrollmentService.getCourseParticipant(course);

        List<CourseParticipantResponse> data = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        CourseParticipantResponse participant;

        if (searchName != null && !searchName.isEmpty()) {
            listEnrollment = listEnrollment.stream()
                    .filter(enrollment -> enrollment.getUser().getNama().toLowerCase().contains(searchName.toLowerCase()))
                    .collect(Collectors.toList());

        }

        for(CourseEnrollmentModel enrollment:listEnrollment) {
            participant = modelMapper.map(enrollment, CourseParticipantResponse.class);
            data.add(participant);
        }

        int start = (page-1) * 10;
        int end = Math.min(start + 10, listEnrollment.size());

        int total = listEnrollment.size();
        List<CourseParticipantResponse> responseData;
        if(start<listEnrollment.size()) {
            responseData = data.subList(start, end);
        } else {
            responseData = null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("participants", data);
        map.put("total", total);

        response = new CustomResponse(200, "Success", map);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/course/delete/{slugName}")
    public ResponseEntity<?> deleteCourse(@PathVariable("slugName") String slugName ) {
        CustomResponse response;
        CourseModel course = courseService.getCourseBySlugName(slugName);
        if(course == null) {
            response = new CustomResponse(404, "Course not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(course.getTotalParticipant() > 0) {
            response = new CustomResponse(500, "Course with participant can't be deleted", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        String image_url = course.getImage_url();
        image_url = image_url.substring(0, image_url.indexOf("/", 7));
        deleteMediaAsync(course.getImage_token(), image_url);

        for (SubCourseModel subCourse : course.getListSubCourse()) {
            for (MaterialModel material : subCourse.getListMaterial()) {
                String url = material.getMaterial_url();
                url = url.substring(0, url.indexOf("/", 7));
                deleteMediaAsync(material.getToken(), url);
            }
        }
        courseService.deleteCourse(course);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/subcourse/delete/{uuid}")
    public ResponseEntity<?> deleteSubCourse(@PathVariable("uuid") String uuid ) {
        CustomResponse response;
        SubCourseModel subcourse = subCourseService.getSubCourseByUuid(uuid);
        if(subcourse == null) {
            response = new CustomResponse(404, "Subcourse not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        for (MaterialModel material : subcourse.getListMaterial()) {
            String url = material.getMaterial_url();
            url = url.substring(0, url.indexOf("/", 7));
            deleteMediaAsync(material.getToken(), url);
        }
        subCourseService.deleteSubCourse(subcourse);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/material/delete/{uuid}")
    public ResponseEntity<?> deleteMaterial(@PathVariable("uuid") String uuid ) {
        CustomResponse response;
        MaterialModel material = materialService.getMaterialByID(uuid);
        if(material == null) {
            response = new CustomResponse(404, "Material not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        String url = material.getMaterial_url();
        url = url.substring(0, url.indexOf("/", 7));
        deleteMediaAsync(material.getToken(), url);
        materialService.deleteMaterial(material);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/discussion/delete/{discussionID}")
    public ResponseEntity<?> deleteDiscussion(@PathVariable("discussionID") String discussionID) {
        CustomResponse response;
        DiscussionModel discussion = discussionService.getDiscussionByID(discussionID);
        if(discussion == null) {
            response = new CustomResponse(404, "Discussion not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        discussionService.deleteDiscussion(discussion);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reply/delete/{replyID}")
    public ResponseEntity<?> deleteReply(@PathVariable("replyID") String replyID) {
        CustomResponse response;
        ReplyModel reply = replyService.getReplyByID(replyID);
        if(reply == null) {
            response = new CustomResponse(404, "Reply not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        replyService.deleteReply(reply);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activity")
    public ResponseEntity<?> getActivity(@RequestParam(defaultValue = "1") int page, @RequestParam(name = "user") String searchUser,
                                         @RequestParam(name = "course") String searchCourse) {
        CustomResponse response;

        List<CourseActivityModel> filteredActivities = new ArrayList<>(courseActivityService.getAllActivity());
        filteredActivities = filteredActivities.stream()
                .filter(activity -> activity.getUser().getUuid().equalsIgnoreCase(searchUser.toLowerCase()))
                .collect(Collectors.toList());

        filteredActivities = filteredActivities.stream()
                .filter(activity -> activity.getCourse().equalsIgnoreCase(searchCourse.toLowerCase()))
                .collect(Collectors.toList());



        int start = (page-1) * 10;
        int end = Math.min(start + 10, filteredActivities.size());

        int total = filteredActivities.size();
        List<CourseActivityModel> data;
        if(start<total) {
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

    private final String AUTH_TOKEN = "Token 24a213a1e3ae916f2e2fbe4c2a7ddab734861465";
    private final String AUTH_TOKEN_2 = "Token d1716bdc2f214d240f4750df95e663d809258ad3";
    private final Map<String, String> TOKEN_MAP = Map.ofEntries(
            Map.entry("http://192.168.1.11", AUTH_TOKEN),
            Map.entry("http://192.168.1.12", AUTH_TOKEN_2)
    );
    @PostMapping(value="/upload-media", consumes= MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadMedia(
            @RequestParam("media_file") MultipartFile mediaFile,
            @RequestParam(value="title", required=false) String title,
            @RequestParam(value="delete_token", required=false) String deleteToken,
            @RequestParam(value="material_url", required=false) String materialUrl
    ) throws IOException {
        // Validate if the file is not empty
        if (mediaFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        try {
            // Use the original filename as the title if it's not provided
            if (title == null || title.isEmpty()) {
                title = mediaFile.getOriginalFilename();
            }

            // Check if deleteToken is provided for deletion
            if (deleteToken != null && !deleteToken.isEmpty() && !deleteToken.equals("undefined")) {
                // Call the deletion method asynchronously
                deleteMediaAsync(deleteToken, materialUrl.substring(0, materialUrl.indexOf("/", 7)));
            }

            // Create the request to MediaCMS
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", AUTH_TOKEN);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("media_file", new FileSystemResource(convert(mediaFile)));
            body.add("title", title);

            HttpEntity<MultiValueMap<String, Object>> postEntity = new HttpEntity<>(body, headers);
            HttpEntity<MultiValueMap<String, Object>> authorizationEntity = new HttpEntity<>(headers);

            if (title.endsWith(".mp4")) {
                String responseBody = "";
                String url = "";
                for (Map.Entry<String, String> entry : TOKEN_MAP.entrySet()) {
                    headers.set("Authorization", entry.getValue());
                    postEntity = new HttpEntity<>(body, headers);

                    ResponseEntity<String> mediaPostResponse = restTemplate.exchange(
                            entry.getKey() + "/api/v1/media/",
                            HttpMethod.POST,
                            postEntity,
                            String.class
                    );
                    responseBody = mediaPostResponse.getBody();
                    int urlIndex = responseBody.indexOf("\"url\":");
                    int startIndex = responseBody.indexOf("\"", urlIndex + 6); // +6 to skip past "\"url\":"
                    int endIndex = responseBody.indexOf("\"", startIndex + 1);
                    url += responseBody.substring(startIndex + 1, endIndex).replace("/view?", "/embed?") + ";";
                }
                return ResponseEntity.ok(responseBody.replaceAll("\"url\":\\s*\"[^\"]*\"", "\"url\": \"" + url.substring(0, url.length()-1) + "\""));
            }

            else {
                ResponseEntity<String> response = restTemplate.exchange(
                        "http://192.168.1.13:8000",
                        HttpMethod.POST,
                        authorizationEntity,
                        String.class
                );
                String url = response.getHeaders().getLocation().toString();
                url = url.substring(0, url.length() - 1);

                if (!url.equals("http://192.168.1.11")) {
                    headers.set("Authorization", TOKEN_MAP.get(url));
                    postEntity = new HttpEntity<>(body, headers);
                    authorizationEntity = new HttpEntity<>(headers);
                }

                ResponseEntity<String> mediaPostResponse = restTemplate.exchange(
                        url + "/api/v1/media/",
                        HttpMethod.POST,
                        postEntity,
                        String.class
                );

                // Extract friendly_token from the response
                String friendlyToken = mediaPostResponse.getBody().substring(19, 28);

                // Construct URL for fetching additional information about the media using the friendly_token
                String mediaInfoUrl = url + "/api/v1/media/" + friendlyToken;

                // Call another API to get additional information about the media using the friendly_token
                ResponseEntity<String> mediaInfoResponse = restTemplate.exchange(
                        mediaInfoUrl,
                        HttpMethod.GET,
                        authorizationEntity,
                        String.class
                );

                String combinedResponse = combineResponseBodies(mediaPostResponse.getBody(), mediaInfoResponse.getBody(), url);
                return ResponseEntity.ok(combinedResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the video: " + e.getMessage());
        }
    }

    // Method to convert MultipartFile to File
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    // Method to extract the original_media_url from the JSON string
    private String extractOriginalMediaUrl(String mediaInfoResponse) {
        int urlStartIndex = mediaInfoResponse.indexOf("\"original_media_url\"")+22;
        int closingQuoteIndex = mediaInfoResponse.indexOf("\"size\"")-1;

        if (urlStartIndex < closingQuoteIndex) {
            // Extract the substring between the original_media_url and size fields
            return mediaInfoResponse.substring(urlStartIndex, closingQuoteIndex);
        }
        // Return null if the original_media_url cannot be extracted
        return null;
    }

    // Method to combines the media post response body with the original_media_url extracted from media info response body
    private String combineResponseBodies(String mediaPostResponse, String mediaInfoResponse, String url) {
        String combinedResponse = mediaPostResponse.substring(0,mediaPostResponse.length()-1);

        // Append the original_media_url to the combined response body
        String originalMediaUrl = extractOriginalMediaUrl(mediaInfoResponse);
        if (originalMediaUrl != null) {
            combinedResponse += ",\"original_media_url\":\"" + url + originalMediaUrl + "}";
        } else {
            combinedResponse += ",\"original_media_url\":null}";
        }

        return combinedResponse;
    }

    @Async
    private void deleteMediaAsync(String deleteToken, String url) {
        try {
            // Create the request to MediaCMS
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", TOKEN_MAP.get(url));
            HttpEntity<MultiValueMap<String, Object>> authorizationEntity = new HttpEntity<>(headers);

            // Construct the delete URL
            String deleteUrl = url + "/api/v1/media/" + deleteToken;
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, authorizationEntity, (Class<Object>) null);
        } catch (Exception e) {
        }
    }
}
