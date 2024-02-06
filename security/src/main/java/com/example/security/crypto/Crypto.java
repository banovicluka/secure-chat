package com.example.security.crypto;

import com.example.security.entities.dto.MessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {

    private static final SecretKey aesKey = generateAesKey();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static SecretKey getAesKey(){
        return Crypto.aesKey;
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

    public static KeyPair generateRSAKeyPair(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        }catch (NoSuchAlgorithmException ex){
            throw new RuntimeException("Failed to generate RSA key pair", ex);
        }
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
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

    public static String signMessage(String privateKeyString, String message){
        try{
            PrivateKey privateKey = stringToPrivateKey(privateKeyString);
            String messageString = objectMapper.writeValueAsString(message);
            byte[] bytes = messageString.getBytes(StandardCharsets.UTF_8);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(bytes);
            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        }catch (Exception ex){
            throw new RuntimeException("Failed to sign message request.",ex);
        }
    }

    public static String serializeAndEncryptWithAES(String message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            return encryptWithAES(json);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to encrypt and serialize message", ex);
        }
    }

    private static String encryptWithAES(String text) {
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,aesKey);
            byte[] iv = cipher.getIV();
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        }catch (Exception ex){
            throw new RuntimeException("Failed to encrypt message.",ex);
        }

    }

    public static byte[] encryptAESKey(byte[] aesKey, PublicKey recipientPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, recipientPublicKey);
        return cipher.doFinal(aesKey);
    }

}
