package com.bok.krypto.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

@Slf4j
@Component
public class AddressGenerator {

    @SneakyThrows
    public String generateAddress() {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");

        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        keyGen.initialize(ecSpec);

        KeyPair kp = keyGen.generateKeyPair();
        PublicKey pub = kp.getPublic();
        PrivateKey pvt = kp.getPrivate();

        ECPrivateKey epvt = (ECPrivateKey) pvt;
        String sepvt = adjustTo64(epvt.getS().toString(16)).toUpperCase();


        ECPublicKey epub = (ECPublicKey) pub;
        ECPoint pt = epub.getW();
        String sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
        String sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
        String bcPub = "04" + sx + sy;

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] s1 = sha.digest(bcPub.getBytes(StandardCharsets.UTF_8));

        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] r1 = rmd.digest(s1);

        byte[] r2 = new byte[r1.length + 1];
        r2[0] = 0;
        System.arraycopy(r1, 0, r2, 1, r1.length);

        byte[] s2 = sha.digest(r2);
        byte[] s3 = sha.digest(s2);

        byte[] a1 = new byte[25];
        System.arraycopy(r2, 0, a1, 0, r2.length);
        System.arraycopy(s3, 0, a1, 20, 5);


        String address = Base58.encode(a1);
        log.info("generated address: " + address);
        return address;

    }

    private String adjustTo64(String s) {
        switch (s.length()) {
            case 62:
                return "00" + s;
            case 63:
                return "0" + s;
            case 64:
                return s;
            default:
                throw new IllegalArgumentException("not a valid key: " + s);
        }
    }

}
