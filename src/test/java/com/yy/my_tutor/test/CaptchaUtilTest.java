package com.yy.my_tutor.test;

import com.yy.my_tutor.util.CaptchaUtil;

/**
 * 验证码工具类测试
 */
public class CaptchaUtilTest {
    
    public static void main(String[] args) {
        // 测试生成验证码文本
        String captcha = CaptchaUtil.generateCaptcha();
        System.out.println("生成的验证码: " + captcha);
        
        // 测试生成验证码图片
        String base64Image = CaptchaUtil.generateCaptchaImage(captcha);
        System.out.println("生成的base64图片长度: " + base64Image.length());
        System.out.println("base64图片前缀: " + base64Image.substring(0, 50) + "...");
        
        // 测试一次性生成
        String[] captchaData = CaptchaUtil.generateCaptchaWithImage();
        System.out.println("一次性生成的验证码: " + captchaData[0]);
        System.out.println("一次性生成的图片长度: " + captchaData[1].length());
        
        // 测试验证功能
        boolean isValid = CaptchaUtil.validateCaptcha(captcha, captcha);
        System.out.println("验证码验证结果: " + isValid);
        
        boolean isInvalid = CaptchaUtil.validateCaptcha(captcha, "0000");
        System.out.println("错误验证码验证结果: " + isInvalid);
    }
}

