package com.wt.util.rsa;

import java.io.IOException;


public class Base64Helper {

    public static String encode(byte[] byteArray) {
        sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
        return base64Encoder.encode(byteArray);
    }

    public static byte[] decode(String base64EncodedString) {
        sun.misc.BASE64Decoder base64Decoder = new sun.misc.BASE64Decoder();
        try {
            return base64Decoder.decodeBuffer(base64EncodedString);
        } catch (IOException e) {
            return null;
        }
    }
}