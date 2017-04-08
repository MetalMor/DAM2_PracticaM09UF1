package constants;

/**
 * Constantes usadas en los ejercicios.
 * @author m0r
 */
public class Constants {
    
    public static final String STR_PROVIDER_JCE = "SunJCE";
    public static final String STR_PROVIDER_KEY_FACTORY = "PBKDF2WithHmacSHA256";
    public static final String STR_USER_DIR_PROPERTY_KEY = "user.dir";
    public static final String STR_KEYS_PATH = System.getProperty(STR_USER_DIR_PROPERTY_KEY) + "keys/";
    
    public static final String STR_MODE_SHA1 = "SHA-1";
    public static final String STR_MODE_MD5 = "MD5";
    public static final String STR_MODE_RSA = "RSA";
    public static final String STR_MODE_AES_CIPHER = "AES/ECB/PKCS5Padding";
    public static final String STR_MODE_RSA_CIPHER = "RSA/ECB/PKCS1Padding";
    public static final String STR_MODE_AES_KEY = "AES";
    public static final String STR_MODE_RSA_KEY = "RSA";
    
    public static final int INT_KEY_SIZE = 256;
    public static final int INT_SALT_SIZE = 8;
    public static final int INT_ITERATION_COUNT = 65536;
    
    public static final String STR_HEX_FORMAT = "%02x";
    
    public static final String STR_NOT_PROCESSED_ERROR = "Not processed.";
    public static final String STR_ONLY_NUMBERS_ERROR = "Wrong input: only numbers.";
}
