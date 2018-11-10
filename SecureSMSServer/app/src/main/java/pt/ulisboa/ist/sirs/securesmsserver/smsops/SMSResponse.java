package pt.ulisboa.ist.sirs.securesmsserver.smsops;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.ist.sirs.securesmsserver.services.TransactionService;

public class SMSResponse {

    public static void sendResponse(Context context, String destination, String message) {

        // Log and display the SMS message.
        Log.d(Parser.class.getSimpleName(), "onReceive: " + message);
        Log.d(Parser.class.getSimpleName(), "iban: " + Parser.getIBAN(message));
        Log.d(Parser.class.getSimpleName(), "amount: " + Parser.getFloatAmount(message));
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        TransactionService.startActionTransaction(context, destination, Parser.getIBAN(message),
                Parser.getFloatAmount(message));
    }
}
