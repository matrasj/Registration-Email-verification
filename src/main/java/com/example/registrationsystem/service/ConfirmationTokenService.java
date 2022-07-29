package com.example.registrationsystem.service;

import com.example.registrationsystem.entity.ConfirmationToken;
import com.example.registrationsystem.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

}
