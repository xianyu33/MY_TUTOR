package com.yy.my_tutor.config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class GoDaddyEmailSender {



    public static void send(String email, String code) {
        final String username = "info@mytec-edu.com"; // 替换为你的 GoDaddy 邮箱
        final String password = "mytectutor"; // 替换为你的 GoDaddy 邮箱密码

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "465"); // 或者 587
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465"); // 或者 587
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // 替换为收件人邮箱
            message.setSubject("Your Code - " + code);
            // 邮件内容（包含6位验证码）
            String content = "<p>Hello</p></br> <p>Your code is: <b>" + code + "</b>. Use it to verify your email for Login. </br>" +
                    "<p>Valid for 5 minutes, please do not disclose.</p></br>" +
                    "<p>Yours,</p>" +
                    "<p>MYTUTOR</p>";
            message.setText(content);
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}