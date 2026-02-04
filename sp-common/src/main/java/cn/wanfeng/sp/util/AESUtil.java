package cn.wanfeng.sp.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AESUtil: AES加密算法工具类.
 *
 * @date: 2025-06-04 16:19
 * @author: luozh.wanfeng
 */
public class AESUtil {

    // AES算法标识（ECB模式 + PKCS5填充）
    private static final String AES_ECB_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 密钥长度：128位（AES-128，无需额外JCE权限）
    private static final int AES_KEY_SIZE = 128;

    /**
     * 加密字符
     *
     * @param text 待加密字符
     * @param aesKey 密钥
     * @return 加密后的字符
     */
    public static String encrypt(String text, String aesKey) {
        try {
            // 1. Base64密钥转原始字节数组
            byte[] keyBytes = Base64.getDecoder().decode(aesKey);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            // 2. 初始化加密器（ECB模式）
            Cipher cipher = Cipher.getInstance(AES_ECB_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 3. 加密（明文转UTF-8字节），结果转Base64
            byte[] encryptBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES-ECB加密失败", e);
        }
    }

    /**
     * 解密 ：使用固定秘钥
     *
     * @param encryptText 需解密的字符串
     * @param aesKey 密钥
     * @return 解密后的结果
     */
    public static String decrypt(String encryptText, String aesKey) {
        try {
            // 1. Base64密钥转原始字节数组
            byte[] keyBytes = Base64.getDecoder().decode(aesKey);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            // 2. 初始化解密器（ECB模式）
            Cipher cipher = Cipher.getInstance(AES_ECB_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // 3. 解密（Base64密文转字节→解密→UTF-8明文）
            byte[] cipherBytes = Base64.getDecoder().decode(encryptText);
            byte[] decryptBytes = cipher.doFinal(cipherBytes);
            return new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES-ECB解密失败（大概率密钥不匹配/密文篡改）", e);
        }
    }

    /**
     * 生成16字节（128位）的随机AES密钥，转Base64字符串（方便传输/保存）
     * @return 随机密钥（Base64格式）
     */
    public static String generateRandomAesKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(AES_KEY_SIZE, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            // 密钥字节数组转Base64字符串（便于前端使用）
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }
}
