package pt.ulisboa.ist.sirs.securesmsserver.data;

import android.content.Context;

import java.util.Random;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.data.repositories.TransactionRepository;

public class DatabaseCreator {

    private TransactionRepository transactionRepository;

    public DatabaseCreator(Context context, int numClients) {
        transactionRepository = new TransactionRepository(context);
        createRandomClients(numClients);
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

    private void createRandomClients(int numClients) {
        Client tempClient;
        Phone tempPhone;
        int ibansRange = ibans.length;
        Random random = new Random();

        for (int i = 0; i < numClients; i++) {
            tempClient = new Client();
            tempClient.setIBAN(ibans[random.nextInt(ibansRange)]);
            tempClient.setBalance(100*random.nextInt(50));

            Phone[] phones = new Phone[4];
            int numberOfClientPhones = random.nextInt(4);

            for (int j = 0; j < numberOfClientPhones; j++) {
                tempPhone = new Phone();
                tempPhone.setPhoneNumber(911234000+random.nextInt(999));
                phones[j] = tempPhone;
            }
            transactionRepository.insertClientAndPhones(tempClient, phones);
        }
    }
}
