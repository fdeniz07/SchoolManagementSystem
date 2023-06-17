package com.schoolmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    private static String MAIL_ADDRESS;
    private static String PASSWORD;

    public static void sendMail(String recipient, String mailMessage, String subject) throws MessagingException {

        Properties properties = new Properties();

        // !!! GMAIL SMTP Sunucusu kullanarak e-posta göndermek icin yapilandiriliyor
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.enable", "false");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_ADDRESS, PASSWORD);
            }
        });

        Message message = prepareMessage(session, MAIL_ADDRESS, recipient, mailMessage, subject);
        Transport.send(message);
    }

    private static Message prepareMessage(Session session, String from, String recipient, String mailMessage, String subject) throws MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setText(mailMessage);
        return message;
    }

    @Value("${email.address}") //application.properties den geliyor
    public void setEmail(String email) {
        MAIL_ADDRESS = email;
    }

    @Value("${email.password}") //application.properties den geliyor
    private void setPassword(String password){
        PASSWORD = password;
    }
}



















