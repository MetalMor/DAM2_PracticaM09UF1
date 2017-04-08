package lib;

import constants.Constants;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import util.Functions;

/**
 *
 * @author m0r
 */
public class Crypter {
    
    private SecretKeySpec _key;
    private Cipher _cipher;
    
    private static final String STR_MODE_RSA_CIPHER = Constants.STR_MODE_RSA_CIPHER;
    private static final String STR_PROVIDER_JCE = Constants.STR_PROVIDER_JCE;
    
    public Crypter(String key) {
        _key = Functions.stringToKey(key);
    }
    
    private void init(int encryptMode) {
        try {
            if (_cipher == null) _cipher = Cipher.getInstance(STR_MODE_RSA_CIPHER, STR_PROVIDER_JCE);
            _cipher.init(encryptMode, _key);
        }
        catch(InvalidKeyException | NoSuchAlgorithmException | 
                NoSuchProviderException | NoSuchPaddingException e) {
            e.printStackTrace();
            _key = null;
            _cipher = null;
        }
    }
    
    public String decrypt(byte[] message) {
        try
        {
            init(Cipher.DECRYPT_MODE);
            return new String(_cipher.doFinal(message), StandardCharsets.UTF_8);
        }
        catch(IllegalBlockSizeException | BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public byte[] encrypt(String message)
    {
        try
        {
            init(Cipher.ENCRYPT_MODE);
            return _cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        }
        catch(IllegalBlockSizeException | BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
