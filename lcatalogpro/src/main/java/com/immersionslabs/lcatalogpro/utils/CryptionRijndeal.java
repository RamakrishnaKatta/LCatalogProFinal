package com.immersionslabs.lcatalogpro.utils;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.Cipher.getInstance;

public class CryptionRijndeal {

    private String Password = "IMMERSIONSLABS";

    public String decrypt(String outputString) throws Exception {

        SecretKeySpec key = generatekey(Password);
        Cipher c = getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] textdecoded = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decodedvalue = c.doFinal(textdecoded);
        String plaintext = new String(decodedvalue);
        return plaintext;
    }

    public String encrypt(String Data) throws Exception {
        SecretKeySpec key = generatekey(Password);
        Cipher c = getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedtext = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encryptedtext, Base64.DEFAULT);
        return encryptedvalue;
    }

    public SecretKeySpec generatekey(String Password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
