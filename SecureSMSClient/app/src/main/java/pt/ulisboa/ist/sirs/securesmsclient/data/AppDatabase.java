package pt.ulisboa.ist.sirs.securesmsclient.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import pt.ulisboa.ist.sirs.securesmsclient.data.daos.live.LiveClientDao;
import pt.ulisboa.ist.sirs.securesmsclient.data.daos.live.LiveMovementDao;
import pt.ulisboa.ist.sirs.securesmsclient.data.daos.main.ClientDao;
import pt.ulisboa.ist.sirs.securesmsclient.data.daos.main.MovementDao;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;

@Database(version = 1, entities = {Client.class, Movement.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "Client";

    private static AppDatabase INSTANCE;

    // ClientDao is a class annotated with @Dao.
    public abstract ClientDao clientDao();

    // LiveClientDao is a class annotated with @Dao.
    public abstract LiveClientDao liveClientDao();

    // MovementDao is a class annotated with @Dao.
    public abstract MovementDao movementDao();

    // LiveMovementDao is a class annotated with @Dao.
    public abstract LiveMovementDao liveMovementDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
