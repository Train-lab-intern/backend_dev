package com.trainlab.service.impl;

import com.trainlab.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String FROM_EMAIL = "it.roast.trainlab@gmail.com";
    public final JavaMailSender emailSender;
    private final SimpleMailMessage mailMessage = new SimpleMailMessage();

    private void sendEmail(String toAddress, String subject, String message) {
        mailMessage.setTo(toAddress);
        mailMessage.setFrom(FROM_EMAIL);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        emailSender.send(mailMessage);
    }

    @Override
    public void sendRegistrationConfirmationEmail(String toAddress) {
        String emailSubject = "Подтверждение регистрации";
        String message = "Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:\n" +
                "https://www.it-roast.com/api/v1/users/complete-registration?userEmail=" + toAddress +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";

        sendEmail(toAddress, emailSubject, message);
    }

    @Override
    public void sendNewPassword(String toAddress, String code) {
        String emailSubject = "Вы забыли пароль";
        String message = "Вы захотели изменить пароль, потому что забыли старый.\n" +
                "Вот ваш код подтверждения!\n" + code +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";

        sendEmail(toAddress, emailSubject, message);
    }
}
