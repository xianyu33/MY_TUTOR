package com.yy.my_tutor.util;

import java.util.Random;

/**
 * 验证码工具类
 * @author yy
 */
public class CaptchaUtil {
    
    /**
     * 生成4位数字验证码
     * @return 4位数字验证码字符串
     */
    public static String generateCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        
        for (int i = 0; i < 4; i++) {
            captcha.append(random.nextInt(10));
        }
        
        return captcha.toString();
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
