package com.safecode.andcloud.util;

import java.util.UUID;

/**
 * 生成虚拟机的相关属性
 */
public class DomainAttrUtil {

    public static String generateMACAddress() {
        String uuid = String.join("", UUID.randomUUID().toString().split("-"));
        // aa:aa:aa:aa:aa:aa
        char[] macchar = new char[17];
        macchar[0] = '5';
        macchar[1] = '2';
        for (int i = 0; i < 5; i++) {
            macchar[i * 3 + 2] = ':';
            macchar[i * 3 + 3] = uuid.charAt(i * 2);
            macchar[i * 3 + 4] = uuid.charAt(i * 2 + 1);
        }
        return new String(macchar);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(generateMACAddress());
    }
}
