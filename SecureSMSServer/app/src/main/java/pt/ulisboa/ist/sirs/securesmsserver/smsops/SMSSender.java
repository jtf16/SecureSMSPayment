package pt.ulisboa.ist.sirs.securesmsserver.smsops;

import android.telephony.SmsManager;

import pt.ulisboa.ist.sirs.securesmsserver.security.SecurityManager;

public class SMSSender {

    public static void sendMessage(String destination, String message, String keyAlias) {
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        try {
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
