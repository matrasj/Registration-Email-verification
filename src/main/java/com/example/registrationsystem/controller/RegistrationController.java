package com.example.registrationsystem.controller;

import com.example.registrationsystem.payload.RegistrationRequest;
import com.example.registrationsystem.payload.RegistrationResponse;
import com.example.registrationsystem.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/v1/registration")
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping
    public ResponseEntity<RegistrationResponse> registerAccount(@RequestBody RegistrationRequest
                                                                            registrationRequest) {
        return ResponseEntity.status(CREATED)
                .body(registrationService.registerUser(registrationRequest));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam(name = "token") String token) {
        return ResponseEntity.status(ACCEPTED)
                .body(registrationService.confirmEmail(token));
    }
}
