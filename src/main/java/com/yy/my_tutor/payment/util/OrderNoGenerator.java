package com.yy.my_tutor.payment.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class OrderNoGenerator {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public String generate(String prefix) {
        String ts = LocalDateTime.now().format(FMT);
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return prefix + ts + rand;
    }
}
