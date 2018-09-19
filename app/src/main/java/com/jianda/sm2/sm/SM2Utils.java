package com.jianda.sm2.sm;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;

public class SM2Utils {

    //生成随机秘钥对
    public static void generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        String privateKeyStr = Utils.byteToHex(publicKey.getEncoded());
        String publicKeyStr = Utils.byteToHex(privateKey.toByteArray());
    }

    //数据加密
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }

        if (data == null || data.length == 0) {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);

        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);

//		System.out.println("C1 " + Utils.byteToHex(c1.getEncoded()));
//		System.out.println("C2 " + Utils.byteToHex(source));
//		System.out.println("C3 " + Utils.byteToHex(c3));
        //C1 C2 C3拼装成加密字串
        return Utils.byteToHex(c1.getEncoded()) + Utils.byteToHex(source) + Utils.byteToHex(c3);

    }

    //数据解密
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Utils.byteToHex(encryptedData);
        /***分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Utils.hexStrToBytes(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Utils.hexStrToBytes(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Utils.hexStrToBytes(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);

        //返回解密结果
        return c2;
    }

    //加密
    public static String encrypt(String pubKey, String plainText) {
        try {
            //加密后的String！
            String cipherText = SM2Utils.encrypt(Utils.hexStrToBytes(pubKey), plainText.getBytes());
            return cipherText;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String privateKey, String cipherText) {
        //国密规范正式私钥
        System.out.println("解密: ");
        try {
            return new String(SM2Utils.decrypt(Utils.hexStrToBytes(privateKey), Utils.hexStrToBytes(cipherText)));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
