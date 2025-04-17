package com.yy.my_tutor.common;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class AESUtil {
    private static final String AES_KEY = "9d2b5f8a1e3c7d9b";
    private static final AES aes = SecureUtil.aes("9d2b5f8a1e3c7d9b".getBytes());

    public AESUtil() {
    }



    public static String encryptHttp(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        } else if (!str.startsWith("http")) {
            return str;
        } else {
            String res;
            try {
                res = aes.encryptHex(str);
            } catch (Exception var3) {
                log.error("字符串[{}]加密失败：{}", str, var3.getMessage());
                res = str;
            }

            return res;
        }
    }

    public static String decryptHttp(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        } else if (str.startsWith("http")) {
            return str;
        } else {
            String res;
            try {
                res = aes.decryptStr(HexUtil.decodeHex(str));
            } catch (Exception var3) {
                log.error("字符串[{}]解密失败：{}", str, var3.getMessage());
                res = str;
            }

            return res;
        }
    }

    public static String encryptBase64(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        } else {
            try {
                return aes.encryptBase64(str);
            } catch (Exception var2) {
                log.error("字符串[{}]加密失败：{}", str, var2.getMessage());
                return str;
            }
        }
    }

    public static String decryptBase64(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        } else {
            try {
                return aes.decryptStr(Base64.decode(str));
            } catch (Exception var2) {
                log.error("字符串[{}]解密失败：{}", str, var2.getMessage());
                return str;
            }
        }
    }
}

