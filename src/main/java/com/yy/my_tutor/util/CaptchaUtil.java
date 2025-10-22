package com.yy.my_tutor.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码工具类
 * @author yy
 */
public class CaptchaUtil {
    
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;
    
    /**
     * 生成4位数字验证码
     * @return 4位数字验证码字符串
     */
    public static String generateCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            captcha.append(random.nextInt(10));
        }
        
        return captcha.toString();
    }
    
    /**
     * 生成验证码图片并返回base64编码
     * @param captchaText 验证码文本
     * @return base64编码的图片字符串
     */
    public static String generateCaptchaImage(String captchaText) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // 设置背景色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);
        
        // 绘制验证码
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            // 随机颜色
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g2d.setColor(color);
            
            // 随机位置
            int x = 20 + i * 20 + random.nextInt(10);
            int y = 25 + random.nextInt(10);
            
            // 绘制字符
            g2d.drawString(String.valueOf(captchaText.charAt(i)), x, y);
        }
        
        // 添加干扰线
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), 
                        random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }
        
        // 添加干扰点
        for (int i = 0; i < 50; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.fillOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 2, 2);
        }
        
        g2d.dispose();
        
        // 转换为base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }
    
    /**
     * 生成验证码图片并返回base64编码（重载方法）
     * @return 包含验证码文本和base64图片的数组，[0]为文本，[1]为base64图片
     */
    public static String[] generateCaptchaWithImage() {
        String captchaText = generateCaptcha();
        String base64Image = generateCaptchaImage(captchaText);
        return new String[]{captchaText, base64Image};
    }
    
    /**
     * 验证验证码是否正确
     * @param inputCaptcha 用户输入的验证码
     * @param correctCaptcha 正确的验证码
     * @return 验证结果
     */
    public static boolean validateCaptcha(String inputCaptcha, String correctCaptcha) {
        if (inputCaptcha == null || correctCaptcha == null) {
            return false;
        }
        return inputCaptcha.equals(correctCaptcha);
    }
}

