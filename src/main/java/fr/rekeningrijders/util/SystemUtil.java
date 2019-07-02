package fr.rekeningrijders.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemUtil
{
    private SystemUtil()
    {
        // Can't instantiate
    }

    private static final Logger LOGGER = Logger.getLogger(SystemUtil.class.getName());

    public static String sha256Hash(String stringToHash)
    {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(stringToHash.getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        if(hash != null)
        {
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        return null;
    }
}
