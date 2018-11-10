package pt.ulisboa.ist.sirs.securesmsserver.data.daos.live;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

@Dao
public abstract class LivePhoneDao {

    @Query("SELECT * FROM Phone ORDER BY id DESC")
    public abstract LiveData<List<Phone>> loadAllPhones();

    @Query("SELECT * FROM Phone WHERE phone_number" +
            " LIKE '%' || :part_phone || '%' ORDER BY id DESC")
    public abstract LiveData<List<Phone>> loadPhonesByPartPhone(String part_phone);

    @Query("SELECT * FROM Phone WHERE client_id = :client_id ORDER BY id DESC")
    public abstract LiveData<List<Phone>> loadPhonesByClientId(int client_id);
}
