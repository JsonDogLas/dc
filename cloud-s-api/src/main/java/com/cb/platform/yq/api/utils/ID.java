package com.cb.platform.yq.api.utils;

import abc.certificate.CertificateRequest;

import java.util.Random;
import java.util.UUID;

public class ID {
    static Random i=new Random();

    public static synchronized String getGuid() {
        return UUID.randomUUID().toString().replace(CertificateRequest.M("\u0004"), "");
    }

    public static synchronized Long getRandom(int a) {
        String var1 = "";
        int var2;
        for(int var10000 = var2 = 0; var10000 < a; var10000 = var2) {
            StringBuilder var3 = new StringBuilder(String.valueOf(var1));
            int var10001 = i.nextInt(9);
            ++var2;
            var1 = var3.append(var10001).toString();
        }
        return new Long(var1);
    }

    public static synchronized Long get6bID(){
        Long random=getRandom(6);
        return  random < 100000 ? (random+100000) : random;
    }

}
