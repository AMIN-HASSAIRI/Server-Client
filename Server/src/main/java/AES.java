import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * This class is to implement the AES (Advanced Encryption Standard)
 */
public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    /**
     * Function to Set the secret key (using "AES" algorithm) to perform encryption and decryption
     * @param myKey The symmetric secret key that both the server and the client hold it after
     *              Diffie-Hellman algorithm generated it.
     */
    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    /**
     * Function to encrypt a message that will be sent to the client from the server
     * @param strToEncrypt the string that we will encrypt
     * @param secret the symmetric secret key calculated by Diffie-Hellman (server and client hold it)
     * @return A Cipher text is returned
     */
    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    /**
     * Function to decrypt a message that we will receive from the client
     * @param strToDecrypt the string (Cipher text) that we will decrypt
     * @param secret the symmetric secret key calculated by Diffie-Hellman (server and client hold it)
     * @return the decrypted message (original message) coming from the client
     */
    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
