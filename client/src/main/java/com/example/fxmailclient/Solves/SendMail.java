package com.example.fxmailclient.Solves;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
public class SendMail {

    public static void sendMail(String username, String password, String port, String protocol, String host) throws Exception {
        Properties props = new Properties();
        props.put(String.format("mail.%s.auth", protocol), "true");
        props.put(String.format("mail.%s.starttls.enable", protocol), "true");
        props.put(String.format("mail.%s.host", protocol), host);
        props.put(String.format("mail.%s.port", protocol), port);
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("HUYNQ.B19CN313@stu.ptit.edu.vn", "9Dn3Tp2L");
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setSubject("TEST RECEIVE MAIL");
            message.setText("HELLO, I'M HUY");

            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[]args) throws Exception {
//        sendMail("huynguyen-langd19ptit@outlook.com", "ofueqoeyxuystyhs123", "587", "smtp", "smtp.office365.com");

    }
}