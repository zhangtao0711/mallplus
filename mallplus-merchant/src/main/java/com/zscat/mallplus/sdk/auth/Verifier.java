package com.zscat.mallplus.sdk.auth;

public interface Verifier {
  boolean verify(String serialNumber, byte[] message, String signature);
}
