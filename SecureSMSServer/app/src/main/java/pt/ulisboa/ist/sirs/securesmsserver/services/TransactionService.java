package pt.ulisboa.ist.sirs.securesmsserver.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import pt.ulisboa.ist.sirs.securesmsserver.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.data.repositories.TransactionRepository;
import pt.ulisboa.ist.sirs.securesmsserver.smsops.SMSSender;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class TransactionService extends IntentService {

    private static final String ACTION_TRANSACTION = "transaction";

    private static final String SENDER = "sender";
    private static final String DESTINATION_IBAN = "destinationIban";
    private static final String AMOUNT = "amount";

    public TransactionService() {
        super("TransactionService");
    }

    /**
     * Starts this service to perform action Transaction with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionTransaction(Context context, String sender,
                                              String destIban, float amount) {
        Intent intent = new Intent(context, TransactionService.class);
        intent.setAction(ACTION_TRANSACTION);
        intent.putExtra(SENDER, sender);
        intent.putExtra(DESTINATION_IBAN, destIban);
        intent.putExtra(AMOUNT, amount);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TRANSACTION.equals(action)) {
                final String param1 = intent.getStringExtra(SENDER);
                final String param2 = intent.getStringExtra(DESTINATION_IBAN);
                final float param3 = intent.getFloatExtra(AMOUNT, 0);
                handleActionTransaction(param1, param2, param3);
            }
        }
    }

    /**
     * Handle action Transaction in the provided background thread with the provided
     * parameters.
     */
    private void handleActionTransaction(String sender, String destIban, float amount) {

        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        Phone pho = appDatabase.phoneDao().loadPhoneByPhoneNumber(sender);
        if (pho != null) {
            Client destCli = appDatabase.clientDao().loadClientByIBAN(destIban);
            if (destCli != null) {
                Client senderCli = appDatabase.clientDao().loadClientById(pho.getClientId());
                if (senderCli != null) {
                    TransactionRepository transactionRepository =
                            new TransactionRepository(getApplicationContext());
                    Movement mov = new Movement();
                    mov.setAmount(amount);
                    transactionRepository.insertMovements(destIban, sender, mov);
                    SMSSender.sendMessage(sender, "iban: " + destIban +
                            ", amount: " + amount);
                }
            } else {
                SMSSender.sendMessage(sender, "destination iban not in the database");
            }
        } else {
            SMSSender.sendMessage(sender, "You are not in the database");
        }
    }
}
