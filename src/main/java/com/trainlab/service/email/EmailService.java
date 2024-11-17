package com.trainlab.service.email;

public interface EmailService {

    void sendRegistrationConfirmationEmail(String toAddress);
    void sendNewPassword(String toAddress, String newPassword);
}
