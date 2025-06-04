package cn.wanfeng.sp.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AESUtil: AES加密算法工具类.
 *
 * @date: 2025-06-04 16:19
 * @author: luozh.wanfeng
 */
public class AESUtil {

    /**
     * 编码格式
     */
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 填充类型
     */
    private static final String AES_TYPE = "AES/ECB/PKCS5Padding";
    /**
     * 秘钥
     */
    private static final String DEFAULT_AES_KEY = "wanfeng_730_SimpleProto";

    /**
     * 加密字符，使用默认密钥
     * @param text 待加密字符
     * @return 加密后的字符
     */
    public static String encrypt(String text){
        return encrypt(text, DEFAULT_AES_KEY);
    }

    /**
     * 加密字符
     *
     * @param text 待加密字符
     * @param aesKey 密钥
     * @return 加密后的字符
     */
    public static String encrypt(String text, String aesKey) {
        try {
            //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
            //实例化加密类，参数为加密方式，要写全，PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            //加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(DEFAULT_CHARSET));

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            LogUtil.error("AES加密失败 text:{}", text, e);
            return text;
        }
    }

    /**
     * 解密（使用默认密钥）
     * @param encrypted 待解密字符
     * @return 解密后的字符
     */
    public static String decrypt(String encrypted){
        return decrypt(encrypted, DEFAULT_AES_KEY);
    }

    /**
     * 解密 ：使用固定秘钥
     *
     * @param encrypted 需解密的字符串
     * @param aesKey 密钥
     * @return 解密后的结果
     */
    public static String decrypt(String encrypted, String aesKey) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(AES_TYPE);
            //与加密时不同MODE:Cipher.DECRYPT_MODE，CBC类型的可以在第三个参数传递偏移量zeroIv,ECB没有偏移量
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            return new String(decryptedData, DEFAULT_CHARSET);
        } catch (Exception e) {
            LogUtil.error("AES解密失败 text:{}", encrypted, e);
            return encrypted;
        }
    }

}
