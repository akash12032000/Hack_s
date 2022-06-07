package In_Box;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Practice {

	public static final String ALGORITHM = "AES";
	public static final byte[] keyValue = "ADBSJHJS12547896".getBytes();;

	public static void main(String[] args) throws Exception {

		String name = "Akash";
		String encrypt = encrypt(name);
		System.out.println("Before : "+name);
		System.out.println("After Decryption :" + encrypt);
		System.out.println("After Encryption :" + decrypt(encrypt));
	}

	private static String encrypt(String decryption) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = genrateKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = cipher.doFinal(decryption.getBytes());
		return new String(Base64.getEncoder().encode(encValue));
	}

	private static String decrypt(String decrypt) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Key key = genrateKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] encValue = cipher.doFinal(Base64.getDecoder().decode(decrypt));
		return new String(encValue);
	}

	@SuppressWarnings("unused")
	private static Key genrateKey() {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}

}
