package com.bok.krypto.core;

import lombok.SneakyThrows;
import org.bitcoinj.core.Base58;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

@Component
/**
 * This is probably the most complicated piece of code. Having affairs with public-private key systems is always a bit
 * complicated, because of this we choose to use a pretty "standard" implementation of Public Address Generators.
 * This is the point where libraries such as bitcoin4J and BouncyCastle come in hand, avoiding us to implement really
 * complicated hashing functions. We do not want to reinvent the wheel, we just want it to be a little rounder than it
 * has always been until now.
 */
public class AddressGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //Static method adjustTo64().
    //Only hexadecimal strings with leading 0 are filled, so the total length is 64 characters.
    static private String adjustTo64(String s) {
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

    /**
     * This method is uset to generate a Wallet address.
     *
     * @return a String containing the Krypto Wallet uniquely generated address
     */
    @SneakyThrows
    public String generateWalletAddress() {
        //Creation of an Elliptic Curve (EC) KeyPairGenerator
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");

        //Using the specified elliptic curve is secp256k1.
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        keyGen.initialize(ecSpec);

        //Once obtained KeyPairGenerator, After that, we can createKeyPair. That is, key pairs from which public and private keys can be obtained.
        KeyPair kp = keyGen.generateKeyPair();
        PublicKey pub = kp.getPublic();
        PrivateKey pvt = kp.getPrivate();

        //We can store only the private part of the key, because the public key can be derived from the private key.
        ECPrivateKey epvt = (ECPrivateKey) pvt;
        String sepvt = adjustTo64(epvt.getS().toString(16)).toUpperCase();

        //The public part of the key generated above is encoded as a bitcoin address.
        // First, the ECDSA key is represented by points on the elliptic curve.
        // The X and Y coordinates of the point include the public key.
        // They are connected to “04” at the beginning to represent the public key.
        ECPublicKey epub = (ECPublicKey) pub;
        ECPoint pt = epub.getW();
        String sx = adjustTo64(pt.getAffineX().toString(16)).toUpperCase();
        String sy = adjustTo64(pt.getAffineY().toString(16)).toUpperCase();
        String bcPub = "04" + sx + sy;

        // We now need to execute on the public keySHA-256And thenRIPEMD-160
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] s1 = sha.digest(bcPub.getBytes(StandardCharsets.UTF_8));

        // We useBouncy CastleProvider executionRIPEMD-160, because re-inventing the wheel is not necessary
        MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] r1 = rmd.digest(s1);

        // Next, we need to add a 0x00 version byte at the beginning of the hash.
        byte[] r2 = new byte[r1.length + 1];
        r2[0] = 0;
        System.arraycopy(r1, 0, r2, 1, r1.length);

        // We now need to perform SHA-256 hash twice for the above results.
        byte[] s2 = sha.digest(r2);
        byte[] s3 = sha.digest(s2);

        // The first four bytes of the second hash result are used as address checksum.
        // It’s attached to the aboveRIPEMD160Hash.
        // This is a 25 byte Krypto address.
        byte[] a1 = new byte[25];
        System.arraycopy(r2, 0, a1, 0, r2.length);
        System.arraycopy(s3, 0, a1, 20, 5);

        // We are using now.bitcoin4j Library Base58.encode() method to get the final address.
        return Base58.encode(a1);
    }
}
