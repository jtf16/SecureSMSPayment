package pt.ulisboa.ist.sirs.securesmsserver.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class SecurityManager {

    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String ECDSA_KEY_PAIR_ALIAS = "ECDSAKeyPair";
    private static final String AES_KEY_ALIAS = "AESKey";
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    //private static final String TRANSFORMATION = "AES/CBC/PKCS7PADDING";

    private static KeyStore loadKeyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_PROVIDER);
        ks.load(null);
        return ks;
    }

    public static byte[] signData(byte[] data) throws Exception {

        KeyStore ks = loadKeyStore();
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) ks
                .getEntry(ECDSA_KEY_PAIR_ALIAS, null);
        Signature s = Signature.getInstance(SIGNATURE_ALGORITHM);
        s.initSign(entry.getPrivateKey());
        s.update(data);
        return s.sign();
    }

    public static boolean verifyData(byte[] data, byte[] signature,
                                     byte[] pubKey) throws Exception {
        Signature s = Signature.getInstance(SIGNATURE_ALGORITHM);
        s.initVerify(byteArrayToPubKey(pubKey));
        s.update(data);
        return s.verify(signature);
    }

    public static Pair encryptData(byte[] data) throws Exception {

        KeyStore ks = loadKeyStore();
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) ks
                .getEntry(AES_KEY_ALIAS, null);
        SecretKey secretKey = secretKeyEntry.getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new Pair(cipher.doFinal(data), cipher.getIV());
    }

    public static byte[] decryptData(byte[] data, byte[] iv) throws Exception {

        KeyStore ks = loadKeyStore();
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) ks
                .getEntry(AES_KEY_ALIAS, null);
        SecretKey secretKey = secretKeyEntry.getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        return cipher.doFinal(data);
    }

    public static void createSecretKey() throws Exception {
        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);

        final KeyGenParameterSpec keyGenParameterSpec =
                new KeyGenParameterSpec.Builder(AES_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM,
                                KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE,
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build();
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    public static void createKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, KEYSTORE_PROVIDER);
        kpg.initialize(new KeyGenParameterSpec.Builder(
                ECDSA_KEY_PAIR_ALIAS,
                KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .build());
        kpg.generateKeyPair();
    }

    public static PublicKey byteArrayToPubKey(byte[] publicKey) {
        try {
            return byteArrayToPubKey(publicKey, KeyProperties.KEY_ALGORITHM_EC);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey byteArrayToPubKey(byte[] publicKey, String algorithm)
            throws NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(publicKey);
        try {
            return kf.generatePublic(pkSpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey() {
        try {
            KeyStore ks = loadKeyStore();
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) ks
                    .getEntry(ECDSA_KEY_PAIR_ALIAS, null);
            return entry.getCertificate().getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToString(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    public static byte[] stringToByteArray(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    public static class Pair {

        private byte[] data;
        private byte[] iv;

        Pair(byte[] data, byte[] iv) {
            this.data = data;
            this.iv = iv;
        }

        public byte[] getData() {
            return data;
        }

        public byte[] getIv() {
            return iv;
        }
    }
}
