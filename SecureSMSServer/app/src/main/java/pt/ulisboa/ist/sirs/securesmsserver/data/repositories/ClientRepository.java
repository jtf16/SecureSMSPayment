package pt.ulisboa.ist.sirs.securesmsserver.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import pt.ulisboa.ist.sirs.securesmsserver.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;

public class ClientRepository {

    static AppDatabase appDatabase;

    public ClientRepository(Context context) {
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    public void insertClient(final Client... clients) {
        new GetClientInsertTask().execute(clients);
    }

    public void deleteClient(final Client... clients) {
        new GetClientDeleteTask().execute(clients);
    }

    public void updateClient(final Client... clients) {
        new GetClientUpdateTask().execute(clients);
    }

    private static class GetClientInsertTask extends AsyncTask<Client, Void, Void> {

        @Override
        protected Void doInBackground(Client... clients) {
            appDatabase.clientDao().insertClients(clients);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetClientDeleteTask extends AsyncTask<Client, Void, Void> {

        @Override
        protected Void doInBackground(Client... clients) {
            appDatabase.clientDao().deleteClients(clients);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetClientUpdateTask extends AsyncTask<Client, Void, Void> {

        @Override
        protected Void doInBackground(Client... clients) {
            appDatabase.clientDao().updateClients(clients);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
