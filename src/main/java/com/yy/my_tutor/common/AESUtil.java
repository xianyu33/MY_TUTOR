package com.yy.my_tutor.common;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密工具类
 */
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 密钥长度为128位
    private static final int KEY_SIZE = 128;
    // 密钥 (实际项目中应该存储在配置文件中，并且加载时解密)
    private static final String SECRET_KEY = "MY_TUTOR_AES_KEY";

    /**
     * 加密
     *
     * @param content 待加密内容
     * @return 加密后的内容，Base64编码
     */
    public static String encrypt(String content) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            // 加密
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            // 使用Base64编码
            return Base64Utils.encodeToString(result);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param content 待解密内容，Base64编码
     * @return 解密后的内容
     */
    public static String decrypt(String content) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
            // 解密
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // 初始化密钥生成器，指定密钥长度
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(SECRET_KEY.getBytes());
            kg.init(KEY_SIZE, random);
            // 生成密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }
} 