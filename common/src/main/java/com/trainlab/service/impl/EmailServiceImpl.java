package com.trainlab.service.impl;

import com.trainlab.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public final JavaMailSender emailSender;
    private final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    public void sendEmail(String address, String subject, String message){
        simpleMailMessage.setTo(address);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }

    @Override
    public void sendRegistrationConfirmationEmail(String toAddress) {
        String emailSubject = "Подтверждение регистрации";
        String message = "Спасибо за регистрацию! Пожалуйста, перейдите по ссылке ниже, чтобы завершить регистрацию:\n" +
                "https://test.app.it-roast.com/api/v1/users/complete-registration?userEmail=" + toAddress +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";

        sendEmail(toAddress, emailSubject, message);
    }

    @Override
    public void sendNewPassword(String toAddress, String newPassword) {
        String emailSubject = "Вы забыли пароль";
        String message = "Вы захотели изменить пароль, потому что старый забыли.\n" +
                "Вот ваш новый пароль, пожалуйста, не забывайте!\n" + newPassword +
                "\nС наилучшими пожеланиями,\nКоманда Trainlab";

        sendEmail(toAddress, emailSubject, message);
    }
}
