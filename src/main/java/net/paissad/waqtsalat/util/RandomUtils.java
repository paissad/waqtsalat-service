package net.paissad.waqtsalat.util;

import java.security.MessageDigest;
import java.util.UUID;

public class RandomUtils {

    private RandomUtils() {
    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Encrypt a text by using the SHA-1 algorithm.
     * 
     * @param text
     * @return The encrypted text.
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     */
    public static String encryptText(final String text) throws IllegalStateException, IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("The text cannot be null or empty.");
        }
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            md.reset();
            md.update(text.getBytes("UTF-8"));
            byte[] raw = md.digest();
            final StringBuilder sb = new StringBuilder(raw.length * 2);
            for (int i = 0; i < raw.length; i++) {
                int v = raw[i] & 0xff;
                if (v < 16) sb.append('0');
                sb.append(Integer.toHexString(v));
            }
            return sb.toString();

        } catch (final Exception e) {
            throw new IllegalStateException("Error while encrypting text : " + e.getMessage(), e);
        }
    }
}
