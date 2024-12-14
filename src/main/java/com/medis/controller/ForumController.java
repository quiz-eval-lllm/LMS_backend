package com.medis.controller;

import com.medis.dto.CustomResponse;
import com.medis.model.course.CourseModel;
import com.medis.model.course.DiscussionModel;
import com.medis.model.course.MaterialModel;
import com.medis.model.course.ReplyModel;
import com.medis.model.user.DokterModel;
import com.medis.model.user.UserModel;
import com.medis.security.JwtService;
import com.medis.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/v1/forum")
public class ForumController {

    @Autowired
    MaterialService materialService;

    @Autowired
    DiscussionService discussionService;

    @Autowired
    ReplyService replyService;

    @Autowired
    DokterService dokterService;

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/{materialID}")
    public ResponseEntity<?> createDiscussion(HttpServletRequest request, @PathVariable("materialID") String materialID, @Valid @RequestBody DiscussionModel discussion, BindingResult bindingResult) {
        CustomResponse response;
        if(bindingResult.hasErrors()) {
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
        UserModel user = userService.findByEmail(email);
        discussion.setName(user.getNama());

        MaterialModel material = materialService.getMaterialByID(materialID);
        discussion.setMaterial(material);
        discussion.setCreatedAt(LocalDateTime.now());
        discussion.setUpdatedAt(LocalDateTime.now());

        discussionService.addDiscussion(discussion);

        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{discussionID}")
    public ResponseEntity<?> getDiscussion(@PathVariable("discussionID") String discussionID) {
        CustomResponse response;

        DiscussionModel discussion = discussionService.getDiscussionByID(discussionID);
        if(discussion == null) {
            response = new CustomResponse(404, "discussion not found", discussion);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response = new CustomResponse(200, "Success", discussion);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reply/{discussionID}")
    public ResponseEntity<?> createReply(HttpServletRequest request, @PathVariable("discussionID") String discussionID, @Valid @RequestBody ReplyModel reply, BindingResult bindingResult) {
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
        UserModel user = userService.findByEmail(email);
        reply.setName(user.getNama());

        DiscussionModel discussion = discussionService.getDiscussionByID(discussionID);
        reply.setDiscussion(discussion);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());

        replyService.addReply(reply);

        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }
}
