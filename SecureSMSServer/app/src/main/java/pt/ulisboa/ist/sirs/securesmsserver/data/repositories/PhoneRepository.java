package pt.ulisboa.ist.sirs.securesmsserver.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import pt.ulisboa.ist.sirs.securesmsserver.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

public class PhoneRepository {

    static AppDatabase appDatabase;

    public PhoneRepository(Context context) {
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    public void insertPhoneNumber(final Phone... phones) {
        new GetPhoneNumberInsertTask().execute(phones);
    }

    public void deletePhoneNumber(final Phone... phones) {
        new GetPhoneNumberDeleteTask().execute(phones);
    }

    public void updatePhoneNumber(final Phone... phones) {
        new GetPhoneNumberUpdateTask().execute(phones);
    }

    private static class GetPhoneNumberInsertTask extends AsyncTask<Phone, Void, Void> {

        @Override
        protected Void doInBackground(Phone... phones) {
            appDatabase.phoneDao().insertPhones(phones);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetPhoneNumberDeleteTask extends AsyncTask<Phone, Void, Void> {

        @Override
        protected Void doInBackground(Phone... phones) {
            appDatabase.phoneDao().deletePhones(phones);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetPhoneNumberUpdateTask extends AsyncTask<Phone, Void, Void> {

        @Override
        protected Void doInBackground(Phone... phones) {
            appDatabase.phoneDao().updatePhones(phones);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
