package com.example.registrationsystem.service;

import com.example.registrationsystem.entity.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String targetEmail, ConfirmationToken confirmationToken, String name) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setSubject("Mail confirmation - QubBank");
        simpleMailMessage.setFrom("jackobcompany218@gmail.com");
        simpleMailMessage.setTo(targetEmail);

        try {
            Scanner scanner = new Scanner(new BufferedReader(new FileReader("C:\\Users\\jkobm\\IdeaProjects\\RegistrationSystem\\src\\main\\resources\\static\\email-body.txt")));
            StringBuilder builder = new StringBuilder();

            builder.append("Hi ").append(name);
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            builder.append("\nYou can do it here: http://localhost:8081/api/v1/registration/confirm?token=")
                    .append(confirmationToken.getToken());


            simpleMailMessage.setText(builder.toString());

            javaMailSender.send(simpleMailMessage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
}
