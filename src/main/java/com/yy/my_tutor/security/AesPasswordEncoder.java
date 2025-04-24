package com.yy.my_tutor.security;

import com.yy.my_tutor.common.AESUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AesPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return AESUtil.encryptBase64(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return AESUtil.encryptBase64(rawPassword.toString()).equals(encodedPassword);
    }
}
