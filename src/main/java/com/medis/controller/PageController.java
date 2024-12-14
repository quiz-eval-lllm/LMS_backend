package com.medis.controller;

import com.medis.dto.*;
import com.medis.model.course.CourseEnrollmentModel;
import com.medis.model.user.DokterModel;
import com.medis.security.JwtService;
import com.medis.service.DokterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Controller
@RequestMapping("/api/v1")
public class PageController {

    @Autowired
    DokterService dokterService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DokterModel dokter, BindingResult bindingResult) throws Exception {
        CustomResponse response;
        if(bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }
        if(dokterService.isEmailExist(dokter.getEmail())) {
            response = new CustomResponse(400, "Email already registered", null);
            return ResponseEntity.badRequest().body(response);
        }

        final String secret = "9d12b871f4a8533e5d3a6d9e95d71b20";
        byte[] cipherData = Base64.getDecoder().decode(dokter.getPassword());
        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedData = aesCBC.doFinal(encrypted);
        String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

        dokter.setPassword(decryptedText);
        dokterService.addDokter(dokter);
        response = new CustomResponse(200, "Success", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) throws Exception {
        CustomResponse response;
        if(bindingResult.hasErrors()) {
            response = new CustomResponse(400, "Please fill the required data properly", null);
            return ResponseEntity.badRequest().body(response);
        }

        Authentication authentication;
        try {
            final String secret = "9d12b871f4a8533e5d3a6d9e95d71b20";
            byte[] cipherData = Base64.getDecoder().decode(authRequest.getPassword());
            byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
            SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

            byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedData = aesCBC.doFinal(encrypted);
            String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), decryptedText));
        } catch (AuthenticationException e) {
            response = new CustomResponse(400, "Incorrect email or password", null);
            return ResponseEntity.badRequest().body(response);
        }

        if(authentication.isAuthenticated()) {
            DokterModel dokter = dokterService.findByEmail(authRequest.getEmail());
            boolean isAdmin = true;
            String role = "ADMIN";
            if(dokter != null) {
                isAdmin = false;
                role = "DOKTER";
            }
            String token = jwtService.generateToken(authRequest.getEmail(), role);
            String email = jwtService.extractEmail(token);
            response = new CustomResponse(200, "Success", new TokenResponse(token));
            return ResponseEntity.ok(response);
        } else {
            response = new CustomResponse(500, "Something went wrong", null);
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
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

        ModelMapper modelMapper = new ModelMapper();
        UserInfoResponse profile = modelMapper.map(dokter, UserInfoResponse.class);
        response = new CustomResponse(200, "Success", profile);
        return ResponseEntity.ok(response);
    }

    public static byte[][] GenerateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte)0);
        }
    }
}
