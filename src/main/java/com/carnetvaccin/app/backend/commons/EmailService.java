package com.carnetvaccin.app.backend.commons;

import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//@SessionScoped
public class EmailService {

    private final String SMTP_SERVER = "mail.privateemail.com";
    private final String EMAIL_FROM = "contact@owonafx.com";

    public void sendEmail(String recipient, String subject, String message) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("contact@owonafx.com", "Xavier123s");
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new CarnetException("Error sending a message: " + e.getMessage());
        }
    }
}