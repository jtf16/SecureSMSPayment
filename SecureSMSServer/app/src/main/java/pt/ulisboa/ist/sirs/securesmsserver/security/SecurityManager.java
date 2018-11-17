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

public class SecurityManager {

    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String ECDSA_KEY_PAIR_ALIAS = "ECDSAKeyPair";
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";

    private static KeyStore loadKeyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_PROVIDER);
        ks.load(null);
        return ks;
    }

    public static byte[] signData(byte[] data) throws Exception {

        KeyStore ks = loadKeyStore();
        KeyStore.Entry entry = ks.getEntry(ECDSA_KEY_PAIR_ALIAS, null);
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            return null;
        }
        Signature s = Signature.getInstance(SIGNATURE_ALGORITHM);
        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
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
            KeyStore.Entry entry = ks.getEntry(ECDSA_KEY_PAIR_ALIAS, null);
            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                return null;
            }
            return ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
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
}
