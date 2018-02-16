package pt.ulisboa.ist.sirs.securesmsserver.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import pt.ulisboa.ist.sirs.securesmsserver.data.daos.ClientDao;
import pt.ulisboa.ist.sirs.securesmsserver.data.daos.MovementDao;
import pt.ulisboa.ist.sirs.securesmsserver.data.daos.PhoneDao;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

@Database(version = 1, entities = {Client.class, Movement.class, Phone.class})
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "Server";

    private static AppDatabase INSTANCE;

    // ClientDao is a class annotated with @Dao.
    public abstract ClientDao clientDao();
    // MovementDao is a class annotated with @Dao.
    public abstract MovementDao movementDao();
    // PhoneDao is a class annotated with @Dao.
    public abstract PhoneDao phoneDao();

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