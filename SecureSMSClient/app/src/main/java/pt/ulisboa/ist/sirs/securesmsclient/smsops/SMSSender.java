package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import android.telephony.SmsManager;

public class SMSSender {

    // The default destination phone number.
    private static final String defaultDestination = "5554";

    public static void sendMessage(String message) {
        sendMessage(defaultDestination, message);
    }

    public static void sendMessage(String destination, String message) {
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destination, null, message,
                null, null);
    }
}
