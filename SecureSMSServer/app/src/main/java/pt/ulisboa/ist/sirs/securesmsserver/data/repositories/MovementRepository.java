package pt.ulisboa.ist.sirs.securesmsserver.data.repositories;

import android.content.Context;
import android.os.AsyncTask;

import pt.ulisboa.ist.sirs.securesmsserver.data.AppDatabase;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;

public class MovementRepository {

    static AppDatabase appDatabase;

    public MovementRepository(Context context) {
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    public void insertMovement(final Movement... movements) {
        new GetMovementInsertTask().execute(movements);
    }

    public void deleteMovement(final Movement... movements) {
        new GetMovementDeleteTask().execute(movements);
    }

    public void updateMovement(final Movement... movements) {
        new GetMovementUpdateTask().execute(movements);
    }

    private static class GetMovementInsertTask extends AsyncTask<Movement, Void, Void> {

        @Override
        protected Void doInBackground(Movement... movements) {
            appDatabase.movementDao().insertMovements(movements);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetMovementDeleteTask extends AsyncTask<Movement, Void, Void> {

        @Override
        protected Void doInBackground(Movement... movements) {
            appDatabase.movementDao().deleteMovements(movements);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class GetMovementUpdateTask extends AsyncTask<Movement, Void, Void> {

        @Override
        protected Void doInBackground(Movement... movements) {
            appDatabase.movementDao().updateMovements(movements);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
