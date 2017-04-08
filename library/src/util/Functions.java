/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import constants.Constants;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author m0r
 */
public class Functions {
    
    private static final int INT_KEY_SIZE = Constants.INT_KEY_SIZE;
    private static final int INT_SALT_SIZE = Constants.INT_SALT_SIZE;
    private static final int INT_ITERATION_COUNT = Constants.INT_ITERATION_COUNT;
    private static final String STR_PROVIDER_KEY_FACTORY = Constants.STR_PROVIDER_KEY_FACTORY;
    private static final String STR_MODE_RSA_KEY = Constants.STR_MODE_RSA_KEY;
    
    public static String byteToHex(final byte[] hash)
    {
        String result;
        try (Formatter formatter = new Formatter()) {
            for (byte b : hash)
                formatter.format(Constants.STR_HEX_FORMAT, b);
            result = formatter.toString();
        }
        return result;
    }
    
    public static SecretKeySpec stringToKey(String key) {
        try {
            String salt = Arrays.toString(new SecureRandom().generateSeed(INT_SALT_SIZE));
            SecretKeyFactory factory = SecretKeyFactory.getInstance(STR_PROVIDER_KEY_FACTORY);
            KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), INT_ITERATION_COUNT, INT_KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), STR_MODE_RSA_KEY);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        StringBuilder output = new StringBuilder(input.length());
        int randPicker;
        
        for (char c : input.toCharArray()) characters.add(c);
        
        while (!characters.isEmpty()) {
            randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
}
