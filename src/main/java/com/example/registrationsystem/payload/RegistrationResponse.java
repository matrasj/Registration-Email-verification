package com.example.registrationsystem.payload;

import com.example.registrationsystem.entity.ConfirmationToken;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private ConfirmationToken confirmationToken;
}
