package com.example.securitylistener.crypto;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Crypto {

    private static SecretKey aesKey = generateAesKey();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setAESKey(SecretKey aesKey){
        Crypto.aesKey = aesKey;
    }
    private static SecretKey generateAesKey() {
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        }catch (NoSuchAlgorithmException ex){
            throw new RuntimeException("Failed to generate AES key", ex);
        }

    }

    public static String decryptAndDeserializeWithAES(String ciphertext) {
        try {
            String decryptedJson = decryptWithAES(ciphertext);
            return objectMapper.readValue(decryptedJson, String.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decrypt and deserialize message", ex);
        }
    }

    public static String decryptWithAES(String ciphertext) {
        try {
            byte[] combined = Base64.getDecoder().decode(ciphertext);
            byte[] iv = Arrays.copyOfRange(combined, 0, 12);
            byte[] encryptedBytes = Arrays.copyOfRange(combined, 12, combined.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);

            return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decrypt message", ex);
        }
    }

    public static byte[] decryptAESKey(byte[] encryptedAESKey, PrivateKey recipientPrivateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, recipientPrivateKey);
        return cipher.doFinal(encryptedAESKey);
    }

    public static PrivateKey stringToPrivateKey(String privKey) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(privKey.getBytes());
        PKCS8EncodedKeySpec PKCS8privateKey = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(PKCS8privateKey);
    }
    public static PublicKey stringToPublicKey(String pubKey) throws Exception {
        byte[] byteKey = Base64.getDecoder().decode(pubKey.getBytes());
        X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(X509publicKey);
    }

    public static boolean verifyMessageSignature(String pubKeyString, String message, String signatureString) {
        try {
            PublicKey publicKey = stringToPublicKey(pubKeyString);

            String messageString = objectMapper.writeValueAsString(message);
            byte[] bytes = messageString.getBytes(StandardCharsets.UTF_8);

            byte[] signatureBytes = Base64.getDecoder().decode(signatureString);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(bytes);

            return signature.verify(signatureBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to verify MessageRequest signature", e);
        }
    }
}
