package com.shuchi.springboot.demo.mycoolapp.rest;

import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PwdEncryptDecrypt {
    public static Logger log = LoggerFactory.getLogger(PwdEncryptDecrypt.class);
    private static String key = "/q3B1HX507uwq1WbckYxad==";
    private static String initVector = "gUelkhAHws3ebxj2DAl+uN==";
    private static HashMap<String, String> pwdMap = new HashMap<>();

    public  String decryptPassword(String encrypted) {
        return decryptPassword(encrypted, key, initVector);
    }

    public  String encryptPassword(String password) {
        return encryptPassword(password, key, initVector);
    }

    private static String decryptPassword(String encryptP, String key, String initVector) {
        if (pwdMap.containsKey(encryptP)) {
            return encryptP;
        }
        byte[] keyInBytes = Base64.getDecoder().decode(key.getBytes());
        byte[] pwdInBytes = Base64.getDecoder().decode(encryptP.getBytes());
        byte[] ivInBytes = Base64.getDecoder().decode(initVector.getBytes());
        SecretKey decKey = new SecretKeySpec(keyInBytes, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, decKey, new IvParameterSpec(ivInBytes));
            byte[] decrypted = cipher.doFinal(pwdInBytes);
            String dec = new String(decrypted);
            if (!pwdMap.containsKey(dec))
                pwdMap.put(dec, encryptP);
            return dec;

        } catch (Exception e) {
            log.error("exception in decrypting password "+encryptP);
            return "Cannot decrypt password "+encryptP;
        }
    }

    private static String encryptPassword(String password, String key0, String initVector0) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey key = null;
            byte[] iv = null;
            iv = Base64.getDecoder().decode(initVector0.getBytes());
            byte[] keyInB64 = Base64.getDecoder().decode(key0.getBytes());
            key = new SecretKeySpec(keyInB64, "AES");
            cipher.init(1, key, new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(password.getBytes());
            byte[] enc = Base64.getEncoder().encode(encrypted);
            return new String(enc);
        } catch (Exception e) {
            log.error("exception in encrypting password " + password);
            return "Please check the password";
        }

    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        PwdEncryptDecrypt.key = key;
    }

    public static String getInitVector() {
        return initVector;
    }

    public static void setInitVector(String initVector) {
        PwdEncryptDecrypt.initVector = initVector;
    }
}