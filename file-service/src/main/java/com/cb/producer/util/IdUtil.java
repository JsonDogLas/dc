package com.cb.producer.util;

import io.micrometer.core.instrument.util.MathUtils;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author jdl
 * @date 2018/9/18 11:12
 */
public class IdUtil {

    public static String getRandomIdByUUID() {
        return UUID.randomUUID().toString().replace("-","").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
        System.out.println(getRandomIdByUUID());
    }
}
