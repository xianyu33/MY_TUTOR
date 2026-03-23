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

    /**
     * 发送注册成功欢迎邮件
     *
     * 模板内容参考需求：
     * Subject: Welcome to MYTutor – You're All Set!
     * Hi [First Name],
     * ...
     *
     * @param email 收件人邮箱
     * @param firstName 名字（插入到模板：Hi [First Name],）
     * @param loginLink 登录链接
     */
    public static void sendWelcomeEmail(String email, String firstName, String loginLink) {
        final String username = "info@mytec-edu.com";
        final String password = "mytectutor";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtpout.secureserver.net");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        String safeFirstName = escapeHtml(firstName);
        String safeLoginLink = escapeHtml(loginLink);

        String subject = "Welcome to MYTutor – You're All Set!";

        // 使用更简单的 HTML/换行，尽量避免 emoji 和特殊字符导致客户端显示异常
        String content = "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#000;\">"
                + "<p>Hi " + safeFirstName + ",</p>"
                + "<p>Welcome to MYTutor! 🎉</p>"
                + "<p>Your account has been successfully created, and you're now ready to start your personalized math learning journey.</p>"
                + "<p>With MYTutor, you can:</p>"
                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;Practice with adaptive learning tailored to your level<br/>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;Get step-by-step guidance whenever you need it<br/>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;Track your progress and see real improvement<br/>"
                + "&nbsp;&nbsp;&nbsp;&nbsp;Stay motivated with fun rewards and achievements</p>"
                + "<p><b>Get started now:</b></p>"
                + "<p>👉 Log in to your account here: " + safeLoginLink + "</p>"
                + "<p>If you have any questions or need help getting started, feel free to reach out to our support team at info@mytec-edu.com. We're here to help every step of the way.</p>"
                + "<p>We're excited to have you with us!</p>"
                + "<p>Best regards,</p>"
                + "<p>The MYTutor Team</p>"
                + "<p>www.mytec-education.com</p>"
                + "<p>www.mytutor.top</p>"
                + "</div>";

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");
            Transport.send(message);
            System.out.println("Welcome email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}