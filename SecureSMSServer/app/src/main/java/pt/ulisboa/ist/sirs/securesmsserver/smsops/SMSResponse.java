package pt.ulisboa.ist.sirs.securesmsserver.smsops;

public class SMSResponse {

    public static void sendResponse(String destination, String message) {
        SMSSender.sendMessage(destination, "OK");
    }
}
