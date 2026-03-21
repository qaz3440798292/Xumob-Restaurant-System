package cn.xumob.restaurant.config;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 密钥对管理
 * 
 * 用于前后端通信加密
 */
@Component
public class RsaKeyPair {

    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;

    // 永久保存的密钥对（生产环境建议存储到配置文件或密钥库）
    private static KeyPair keyPair;

    static {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(KEY_SIZE, new SecureRandom());
            keyPair = keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA 密钥生成失败", e);
        }
    }

    /**
     * 获取公钥（Base64 编码）
     */
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取私钥（Base64 编码）
     */
    public String getPrivateKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
    }

    /**
     * 解密（使用私钥）
     * 
     * @param encryptedText Base64 编码的加密文本
     * @return 解密后的原文
     */
    public String decrypt(String encryptedText) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("RSA 解密失败", e);
        }
    }
}
