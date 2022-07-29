package com.example.registrationsystem.service;

import com.example.registrationsystem.entity.AppUser;
import com.example.registrationsystem.entity.ConfirmationToken;
import com.example.registrationsystem.exception.EmailNotValidException;
import com.example.registrationsystem.exception.TokenAlreadyConfirmedException;
import com.example.registrationsystem.exception.TokenHasBeenAlreadyExpiredException;
import com.example.registrationsystem.exception.TokenNotFoundException;
import com.example.registrationsystem.payload.RegistrationRequest;
import com.example.registrationsystem.payload.RegistrationResponse;
import com.example.registrationsystem.security.AppUserRole;
import com.example.registrationsystem.validation.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private static final String EMAIL_NOT_VALID_MESSAGE = "Email %s is not valid";
    private static final Long EMPTY_ID = null;
    private static final String NOT_FOUND_TOKEN_MESSAGE = "Token %s has not been found";
    private static final String TOKEN_ALREADY_CONFIRMED_MESSAGE = "Token %s have been already confirmed";
    private static final String TOKEN_ALREADY_EXPIRED_MESSAGE = "Token %s already expired";
    private static final Boolean ENABLE_ACCOUNT = true;
    private static final String SUCCESS_TOKEN_CONFIRMATION = "Successfully confirmed token :)";
    private final EmailValidator emailValidator;
    private final AppUserDetailsService appUserDetailsService;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationResponse registerUser(RegistrationRequest registrationRequest) {
        boolean isEmailValid
                = emailValidator.test(registrationRequest.getEmail());

        if (!isEmailValid) {
            throw new EmailNotValidException(String.format(EMAIL_NOT_VALID_MESSAGE, registrationRequest.getEmail()));
        }


        return appUserDetailsService.signUpUser(
                AppUser.builder()
                        .id(EMPTY_ID)
                        .username(registrationRequest.getEmail())
                        .email(registrationRequest.getEmail())
                        .name(registrationRequest.getFirstName()
                                .concat("")
                                .concat(registrationRequest.getLastName()))
                        .role(AppUserRole.USER)
                        .password(registrationRequest.getPassword())
                        .enabled(false)
                        .locked(false)
                        .build()
        );
    }

    public String confirmEmail(String token) {
        // Check if token exists
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException(String.format(NOT_FOUND_TOKEN_MESSAGE, token)));


        // Check if token is not already confirmed
        if (confirmationToken.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(String.format(TOKEN_ALREADY_CONFIRMED_MESSAGE, token));
        }

        // Check if it is not too late to confirm token
        if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new TokenHasBeenAlreadyExpiredException(String.format(TOKEN_ALREADY_EXPIRED_MESSAGE, token));
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.getAppUser().setEnabled(ENABLE_ACCOUNT);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return SUCCESS_TOKEN_CONFIRMATION;
    }
}
