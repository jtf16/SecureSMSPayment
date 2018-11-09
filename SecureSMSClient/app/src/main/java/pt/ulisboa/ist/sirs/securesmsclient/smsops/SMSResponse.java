package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import android.content.Context;
import android.widget.Toast;

public class SMSResponse {
    public static void sendResponse(Context context, String destination, String message) {

        // Log and display the SMS message.
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        boolean a = false;
        if (a) {
            SMSSender.sendMessage(destination, "");
        }
    }
}
