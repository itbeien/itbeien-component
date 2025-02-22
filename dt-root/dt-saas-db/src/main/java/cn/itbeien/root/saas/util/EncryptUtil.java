package cn.itbeien.root.saas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 支持base64,MD5,DES加解密
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@SuppressWarnings("restriction")
public class EncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public final static String MD5ToString(String signed) {

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {

			byte[] res = signed.getBytes(StandardCharsets.UTF_8);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
			mdTemp.update(res);
			byte[] md = mdTemp.digest();

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static char[] md416to10(byte[] md) {
		int j = md.length;
		char[] str = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return str;
	}

	public final static byte[] MD5(String str) {
		try {
			byte[] res = str.getBytes(StandardCharsets.UTF_8);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
			mdTemp.update(res);
			byte[] hash = mdTemp.digest();
			return hash;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	   * MD5值计算<p>
	   * MD5的算法在RFC1321 中定义:
	   * 在RFC 1321中，给出了Test suite用来检验你的实现是否正确：
	   * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
	   * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
	   * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
	   * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
	   * MD5 ("abcdefghijklmnopqrstuvwxyz") = c3fcd3d76192e4007dfb496cca67e13b
	   *
	   * @return md5值
	   */
	public final static byte[] MD5EncrtyReutrnhexDigitsByteArray(String str) {
		try {
			byte[] res = str.getBytes(StandardCharsets.UTF_8);
			MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
			mdTemp.update(res);
			byte[] hash = mdTemp.digest();
			return hash;
		} catch (Exception e) {
			return null;
		}
	}

	public final static String MD5EncrtyReturnString(String str) {

		byte[] b = MD5EncrtyReutrnhexDigitsByteArray(str);

		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int n = b[i];
			if (n < 0)
				n = 256 + n;
			int d1 = n / 16;
			int d2 = n % 16;
			resultSb.append(hexDigits[d1]);
			resultSb.append(hexDigits[d2]);
		}
		return resultSb.toString();

	}

	// 加密后解密
	public static String JM(byte[] inStr) {
		String newStr = new String(inStr);
		char[] a = newStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	/**
	 * BASE64加密MD5EncrtyReutrnhexDigitsByteArray
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String BASE64Encrypt(byte[] key) throws Exception {
		String edata = null;
		try {
			edata = Base64.getEncoder().encodeToString(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return edata;
	}

	/**
	 * BASE64解密
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static byte[] BASE64Decrypt(String data) {
		if (data == null)
			return null;
		byte[] edata = null;
		try {
			edata = Base64.getDecoder().decode(data);
			return edata;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param key 24位密钥
	 * @param str 源字符串
	 * @return
	 */
	public static byte[] DES3Encrypt(String key, String str) throws Exception {

		byte[] bt = null;
		try {
			byte[] newkey = key.getBytes();

			SecureRandom sr = new SecureRandom();

			DESedeKeySpec dks = new DESedeKeySpec(newkey);

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");

			SecretKey securekey = keyFactory.generateSecret(dks);

			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

			bt = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));



		} catch (Exception e) {
			e.printStackTrace();
			logger.error("DES3加密签名错误: " + e.getMessage(), e);
		}
		return bt;
	}

	/**
	 * 解密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String DES3Decrypt(byte[] edata, String key) {
		String data = "";
		try {
			if (edata != null) {
				byte[] newkey = key.getBytes();
				DESedeKeySpec dks = new DESedeKeySpec(newkey);
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
				SecretKey securekey = keyFactory.generateSecret(dks);
				Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, securekey, new SecureRandom());
				// String newData = new String(edata);
				// if (!newData.endsWith("=")){
				// data = URLDecoder.decode(newData,"utf-8");
				// }
				byte[] bb = cipher.doFinal(edata);
				data = new String(bb, StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * <p>
	 * Description: 加密成字符串
	 * </p>
	 *
	 * @param str 需要加密的字符串
	 * @param key 密钥
	 * @return java.lang.String
	 */
	public static String DES3EncryptToStr(String str, String key) throws Exception {
		return java.util.Base64.getEncoder().encodeToString(DES3Encrypt(key, str));
	}

	/**
	 * <p>
	 * Description: 解密成字符串
	 * </p>
	 *
	 * @param str 加密的字符串
	 * @param key 密钥
	 * @return java.lang.String
	 * @author 袁兴旺（c332030）
	 */
	public static String DES3DecryptFromStr(String str, String key) {
		return EncryptUtil.DES3Decrypt(java.util.Base64.getDecoder().decode(str), key);
	}

	/**
	 * 方法用途: 签名加密<br>
	 * 实现步骤: <br>
	 * @param signed ：签名的字符串
	 * @return
	 */
	public static String encryptSigned(String signed) throws Exception {
		String b64SignStr = null;
		try {
			byte[] md5SignStr = MD5EncrtyReutrnhexDigitsByteArray(signed);
			 b64SignStr = BASE64Encrypt(md5SignStr);

		} catch (Exception e) {

		}
		return b64SignStr;
	}

	public static String encryptSignedSelfTerminal(String signed) throws Exception {
		String b64SignStr = BASE64Encrypt(MD5ToString(signed).getBytes());
		return b64SignStr;
	}

	/**
	 * 解密
	 *
	 * @param decryptKey
	 * @param source
	 * @return
	 */
	public static String decrypt(String decryptKey, String source) {
		try {
			byte[] raw = decryptKey.getBytes(StandardCharsets.UTF_8);
			SecretKey skey = new SecretKeySpec(raw, "DESede");
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			byte[] encrypted1 = Base64.getDecoder().decode(source);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, StandardCharsets.UTF_8);
			return originalString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String args[]){

		System.out.println(EncryptUtil.DES3DecryptFromStr("VHFn1VvE+ymTNcwvx4XCaw==","123456789012345678901234"));
	}

}
