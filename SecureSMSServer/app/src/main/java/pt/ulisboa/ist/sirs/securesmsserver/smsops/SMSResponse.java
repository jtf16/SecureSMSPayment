package pt.ulisboa.ist.sirs.securesmsserver.smsops;

import android.content.Context;

import pt.ulisboa.ist.sirs.securesmsserver.services.TransactionService;

public class SMSResponse {

    public static final int TRANSACTION_OK = 0;
    public static final int NONEXITENT_DESTINATION = -1;
    public static final int NONEXITENT_SENDER = -2;
    public static final int INSUFFICIENT_BALANCE = -3;
    public static final int NO_MOVEMENTS = -4;

    public static void handleReceived(Context context, String destination, String message) {

        TransactionService.startActionTransaction(context, destination, Parser.getIBAN(message),
                Parser.getFloatAmount(message));
    }

    public static void handleTransaction(String sender, int statusId) {
        // If statusId >= 0 (TRANSACTION_OK) then it's the balance else it's an error
        String message = Math.abs(statusId) + "";
        if (statusId >= TRANSACTION_OK) {
            message = TRANSACTION_OK + new StringBuffer(statusId + "").reverse().toString();
        }
        SMSSender.sendMessage(sender, Parser.parseMessage(message));
    }
}
