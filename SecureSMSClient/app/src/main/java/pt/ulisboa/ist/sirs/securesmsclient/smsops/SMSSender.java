package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import android.telephony.SmsManager;

import java.security.SecureRandom;

import pt.ulisboa.ist.sirs.securesmsclient.security.SecurityManager;

public class SMSSender {

    // The default destination phone number.
    private static final String defaultDestination = "5554";
    private static String messageToSend = "";

    public static void sendMessage(String message, String keyAlias) {
        sendMessage(defaultDestination, message, keyAlias);
    }

    public static void sendMessage(String destination, String message, String keyAlias) {
        try {
            if (keyAlias.equals(SecurityManager.SHARED_KEY)) {
                messageToSend = message;
                SecureRandom random = new SecureRandom();
                byte bytes[] = new byte[20];
                random.nextBytes(bytes);
                message = SecurityManager.byteArrayToString(bytes);
                SecurityManager.createSecretKey(SecurityManager.hashData(message.getBytes()),
                        SecurityManager.SESSION_KEY);
            } else if (keyAlias.equals(SecurityManager.SESSION_KEY)) {
                message = messageToSend;
            }
            // Use SmsManager.
            SmsManager smsManager = SmsManager.getDefault();

            SecurityManager.Pair pair = SecurityManager.encryptData(
                    message.getBytes(), keyAlias);
            String sEnData = SecurityManager.byteArrayToString(pair.getData());
            String sIv = SecurityManager.byteArrayToString(pair.getIv());

            byte[] hashData = SecurityManager.hashData(message.getBytes());
            String shash = SecurityManager.byteArrayToString(hashData);

            message = shash + sIv + sEnData;
            smsManager.sendTextMessage(destination, null, message,
                    null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
