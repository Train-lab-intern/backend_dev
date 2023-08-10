package com.trainlab.service;

public interface EmailService {

    void sendRegistrationConfirmationEmail(String toAddress);
    void sendNewPassword(String toAddress, String newPassword);
}
