package com.leocode.the_java_academy_bank.service;

import com.leocode.the_java_academy_bank.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
}
