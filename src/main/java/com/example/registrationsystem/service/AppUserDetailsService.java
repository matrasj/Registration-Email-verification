package com.example.registrationsystem.service;

import com.example.registrationsystem.entity.AppUser;
import com.example.registrationsystem.entity.ConfirmationToken;
import com.example.registrationsystem.exception.EmailAlreadyTakenException;
import com.example.registrationsystem.exception.UserNotFoundException;
import com.example.registrationsystem.payload.RegistrationResponse;
import com.example.registrationsystem.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private static final String USER_NOT_FOUND_MESSAGE = "Not found user with %s email";
    private static final String EMAIL_ALREADY_TAKEN_MESSAGE = "Email %s is already taken";
    private final Long EMPTY_ID = null;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    @Transactional
    public RegistrationResponse signUpUser(AppUser appUser) {
        // check if user already exists

        boolean alreadyExist
                = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();

        if (alreadyExist) {
            throw new EmailAlreadyTakenException(String.format(EMAIL_ALREADY_TAKEN_MESSAGE, appUser.getEmail()));
        }

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
        // send confirmation token

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .id(EMPTY_ID)
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .appUser(appUser)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // send email
        emailSenderService.sendEmail(appUser.getEmail(), confirmationToken, appUser.getName());

        return  new RegistrationResponse(confirmationToken);
    }
}
