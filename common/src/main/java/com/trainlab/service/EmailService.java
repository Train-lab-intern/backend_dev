package com.trainlab.service;

public interface EmailService {

    void sendRegistrationConfirmationEmail(String toAddress, String subject, String message);
}
