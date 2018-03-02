package pt.ulisboa.ist.sirs.securesmsclient.data;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;
import pt.ulisboa.ist.sirs.securesmsclient.data.repositories.ClientRepository;
import pt.ulisboa.ist.sirs.securesmsclient.data.repositories.MovementRepository;

public class DatabaseCreator {

    private ClientRepository clientRepository;
    private MovementRepository movementRepository;

    public DatabaseCreator(Context context, int numMovements) {
        clientRepository = new ClientRepository(context);
        movementRepository = new MovementRepository(context);
        createRandomClient();
        createRandomMovements(numMovements);
    }

    private String[] ibans = new String[]{
            "PT50 0002 0123 1234 5678 9015 4", "PT50 0002 0123 1234 5678 9025 1",
            "PT50 0002 0123 1234 5678 9034 8", "PT50 0002 0123 1234 5678 9044 5",
            "PT50 0002 0123 1234 5678 9054 2", "PT50 0002 0123 1234 5678 9063 9",
            "PT50 0002 0123 1234 5678 9073 6", "PT50 0002 0123 1234 5678 9083 3",
            "PT50 0002 0123 1234 5678 9093 0", "PT50 0002 0123 1234 5678 9102 7",
            "PT50 0002 0123 1234 5678 9112 4", "PT50 0002 0123 1234 5678 9122 1",
            "PT50 0002 0123 1234 5678 9131 8", "PT50 0002 0123 1234 5678 9141 5",
            "PT50 0002 0123 1234 5678 9151 2", "PT50 0002 0123 1234 5678 9160 9",
            "PT50 0002 0123 1234 5678 9170 6", "PT50 0002 0123 1234 5678 9180 3",
            "PT50 0002 0123 1234 5678 9190 0", "PT50 0002 0123 1234 5678 9209 4",
            "PT50 0002 0123 1234 5678 9219 1", "PT50 0002 0123 1234 5678 9228 8",
            "PT50 0002 0123 1234 5678 9238 5", "PT50 0002 0123 1234 5678 9248 2",
            "PT50 0002 0123 1234 5678 9257 9", "PT50 0002 0123 1234 5678 9267 6",
            "PT50 0002 0123 1234 5678 9277 3", "PT50 0002 0123 1234 5678 9287 0",
            "PT50 0002 0123 1234 5678 9286 7", "PT50 0002 0123 1234 5678 9306 4"
    };

    private String[] states = new String[]{
            "Processed", "Cancelled"
    };

    private void createRandomClient() {
        int ibansRange = ibans.length;
        Random random = new Random();

        Client tempClient = new Client();
        tempClient.setUid(0);
        tempClient.setIBAN(ibans[random.nextInt(ibansRange)]);
        tempClient.setBalance(100 * random.nextInt(50));

        clientRepository.insertClient(tempClient);
    }

    private void createRandomMovements(int numMovements) {

        int ibansRange = ibans.length;
        int statesRange = states.length;
        Random random = new Random();
        Movement tempMovement;

        long millisecondsYear = TimeUnit.DAYS.toMillis(365);

        Calendar cLastYear = Calendar.getInstance();
        long start = cLastYear.getTimeInMillis() - millisecondsYear;

        for (int i = 0; i < numMovements; i++) {

            long rand = Math.abs(random.nextLong() % (millisecondsYear));

            tempMovement = new Movement();
            tempMovement.setFromOrTo(random.nextInt(2));
            tempMovement.setIBAN(ibans[random.nextInt(ibansRange)]);
            tempMovement.setAmount(100 * random.nextInt(50));
            tempMovement.setDate(new Date(start + rand));
            tempMovement.setState(states[random.nextInt(statesRange)]);

            movementRepository.insertMovement(tempMovement);
        }
    }
}
