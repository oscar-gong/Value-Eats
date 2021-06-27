package com.nuggets.valueeats.utils;

import org.apache.commons.codec.digest.DigestUtils;

public final class EncryptionUtils {
    public static String encrypt(final String string, final String secret) {
        return DigestUtils.sha256Hex(string + secret);
    }
}
