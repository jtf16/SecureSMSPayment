package pt.ulisboa.ist.sirs.securesmsclient.smsops;

import android.content.Context;
import android.widget.Toast;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;
import pt.ulisboa.ist.sirs.securesmsclient.data.repositories.ClientRepository;
import pt.ulisboa.ist.sirs.securesmsclient.security.SecurityManager;

public class SMSResponse {

    public static final int TRANSACTION_OK = 0;
    public static final int NONEXITENT_DESTINATION = -1;
    public static final int NONEXITENT_SENDER = -2;
    public static final int INSUFFICIENT_BALANCE = -3;
    public static final int NO_MOVEMENTS = -4;
    public static final int REPLAY_ATTACK = -5;

    public static void handleReceived(Context context, String destination, String message) {

        try {
            String log = "";
            String hash = message.substring(0, 44);
            String iv = message.substring(44, 60);
            String enData = message.substring(60);

            byte[] bEnData = SecurityManager.stringToByteArray(enData);
            byte[] bEnIv = SecurityManager.stringToByteArray(iv);
            byte[] decrypted = SecurityManager.decryptData(
                    bEnData, bEnIv, SecurityManager.SESSION_KEY);
            message = new String(decrypted);

            if (SecurityManager.verifyHash(message.getBytes()
                    , SecurityManager.stringToByteArray(hash))) {
                if (message.equals("Ok")) {
                    SMSSender.sendMessage("", SecurityManager.SESSION_KEY);

                } else {
                    switch (Parser.getCode(message)) {
                        case TRANSACTION_OK:
                            // TODO: Update client fields correctly
                            ClientRepository clientRepository = new ClientRepository(context);
                            Client tempClient = new Client();
                            tempClient.setUid(0);
                            tempClient.setBalance(Parser.getFloatBalance(message));
                            clientRepository.updateClient(tempClient);
                            log = "You're current balance is: " + Parser.getFloatBalance(message);
                            break;
                        case -NONEXITENT_DESTINATION:
                            log = "No such destiny";
                            break;
                        case -NONEXITENT_SENDER:
                            log = "You're not on the server";
                            break;
                        case -INSUFFICIENT_BALANCE:
                            log = "Not enough balance";
                            break;
                        case -NO_MOVEMENTS:
                            log = "Couldn't perform that movement";
                            break;
                        case -REPLAY_ATTACK:
                            log = "Nounce already used";
                            break;
                    }
                    Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
