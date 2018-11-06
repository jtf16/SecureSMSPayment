package pt.ulisboa.ist.sirs.securesmsserver.smsops;

import android.telephony.SmsManager;

public class SMSSender {

    public static void sendMessage(String destination, String message) {
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destination, null, message,
                null, null);
    }
}
