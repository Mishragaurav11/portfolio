package com.myportfolio.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    
    
    @Value("${spring.mail.username}")
    private String fromMail;
    // ⭐ Contact Mail To You (HTML)
    public void sendContactMailToMe(String name, String email, String subject, String message) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromMail);
        helper.setTo(fromMail);
        helper.setReplyTo(email);

        helper.setSubject("📩 New Portfolio Contact");

        String htmlContent =
                "<h2>New Contact Request</h2>" +
                "<p><b>Name:</b> " + name + "</p>" +
                "<p><b>Email:</b> " + email + "</p>" +
                "<p><b>Subject:</b> " + subject + "</p>" +
                "<p><b>Message:</b><br>" + message + "</p>";

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    // ⭐ Auto Reply HTML Mail
    public void sendAutoReplyToUser(String userEmail, String userName) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromMail);
        helper.setTo(userEmail);

        helper.setSubject("✅ Thank You for Contacting Me");

        String htmlContent =
                "<div style='font-family:Arial;padding:20px'>" +
                "<h2 style='color:#4CAF50'>Thank You " + userName + " 🙌</h2>" +

                "<p>I have received your message successfully.</p>" +
                "<p>I will get back to you soon.</p>" +

                "<hr>" +

                "<p style='color:gray;font-size:13px'>" +
                "This is an auto generated email.<br>" +
                "Please do not reply to this email." +
                "</p>" +

                "<h3>Gaurav Mishra</h3>" +
                "<p>Java | Spring Boot Developer</p>" +
                "</div>";

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
