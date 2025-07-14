package com.yy.my_tutor.config;




import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    // 邮件服务器配置（QQ邮箱）
    private static final String HOST = "smtp.qq.com";
    private static final int PORT = 465; // SSL加密端口
    private static final String USERNAME = "100049846@qq.com"; // 你的QQ邮箱
    private static final String PASSWORD = "cfmseyytjwgkbhha";   // 邮箱授权码（非登录密码）

    /**
     * 发送验证码邮件
     * @param toEmail 收件人邮箱
     * @param code    验证码
     */
    public static void sendVerificationCode(String toEmail, String code) throws MessagingException {
        // 1. 配置邮件服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // 启用SSL加密
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        // 2. 创建Session会话（传入发件人账号和授权码）
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        // 3. 创建邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(USERNAME));
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        // 邮件主题
        message.setSubject("Your Code - " + code);
        // 邮件内容（包含6位验证码）
        String content = "<p>Hello</p></br> <p>Your code is: <b>" + code + "</b>. Use it to verify your email for Login. </br>" +
                "<p>Valid for 5 minutes, please do not disclose.</p></br>" +
                "<p>Yours,</p>" +
                "<p>MYTUTOR</p>";
        message.setContent(content, "text/html;charset=UTF-8");

        // 4. 发送邮件
        Transport.send(message);
    }
}
