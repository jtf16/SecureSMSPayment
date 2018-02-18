package pt.ulisboa.ist.sirs.securesmsserver.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import pt.ulisboa.ist.sirs.securesmsserver.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

public class TransactionRepository {

    static AppDatabase appDatabase;

    public TransactionRepository(Context context) {
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    public void insertClientAndPhones(Client client, Phone... phones) {
        new InsertClientPhonesTask(client).execute(phones);
    }

    public void insertPhonesByIBAN(String IBAN, Phone... phones) {
        new InsertPhonesByIBANTask(IBAN).execute(phones);
    }

    public void insertMovements(String IBANTo, int phoneFrom, Movement... movements) {
        new InsertMovementsTask(IBANTo, phoneFrom).execute(movements);
    }

    private static class InsertClientPhonesTask extends AsyncTask<Phone, Void, Void> {

        private Client client;

        InsertClientPhonesTask(Client client) {
            this.client = client;
        }

        @Override
        protected Void doInBackground(Phone... phones) {
            appDatabase.transactionDao().insertClientAndPhones(client, phones);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class InsertPhonesByIBANTask extends AsyncTask<Phone, Void, Void> {

        private String IBAN;

        InsertPhonesByIBANTask(String IBAN) {
            this.IBAN = IBAN;
        }

        @Override
        protected Void doInBackground(Phone... phones) {
            appDatabase.transactionDao().insertPhonesByIBAN(IBAN, phones);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class InsertMovementsTask extends AsyncTask<Movement, Void, Void> {

        private String IBANTo;
        private int phoneFrom;

        InsertMovementsTask(String IBANTo, int phoneFrom) {
            this.IBANTo = IBANTo;
            this.phoneFrom = phoneFrom;
        }

        @Override
        protected Void doInBackground(Movement... movements) {
            appDatabase.transactionDao().insertMovements(IBANTo, phoneFrom, movements);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
