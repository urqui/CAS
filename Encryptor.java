package org.jasig.cas.adaptors.jdbc;

/**
 *
 * @author Kinesis Identity Security System Inc.
 */
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

    final byte[] encryptionkey;

    byte[] iv;

    public Encryptor(String key, byte[] iv) {

        // convert text to binary. 
        encryptionkey = urquiCommon.hex2bin(key);

        this.iv = iv;
    }

    public byte[] encrypt(String plainText) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionkey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public String decrypt(byte[] cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
        SecretKeySpec key = new SecretKeySpec(encryptionkey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }
}
