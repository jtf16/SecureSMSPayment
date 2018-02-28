package pt.ulisboa.ist.sirs.securesmsclient.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import pt.ulisboa.ist.sirs.securesmsclient.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;

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

    public void insertAndDeleteClient(final Client oldClient, final Client newClient) {
        new GetClientDeleteAndInsertTask(oldClient, newClient).execute();
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

    private static class GetClientDeleteAndInsertTask extends AsyncTask<Void, Void, Void> {

        private Client oldClient;
        private Client newClient;

        GetClientDeleteAndInsertTask(Client oldClient, Client newClient) {
            this.oldClient = oldClient;
            this.newClient = newClient;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.clientDao().deleteAndInsertInTransaction(oldClient, newClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
