package pt.ulisboa.ist.sirs.securesmsserver.data.daos.main;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

@Dao
public abstract class PhoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPhones(Phone... phones);

    @Update
    public abstract void updatePhones(Phone... phones);

    @Delete
    public abstract void deletePhones(Phone... phones);

    @Query("SELECT COUNT(*) FROM Phone")
    public abstract int countPhones();

    @Query("SELECT * FROM Phone WHERE id = :id LIMIT 1")
    public abstract Phone loadPhoneById(int id);

    @Query("SELECT * FROM Phone WHERE phone_number LIKE :phone_number LIMIT 1")
    public abstract Phone loadPhoneByPhoneNumber(String phone_number);

    @Query("SELECT * FROM Phone ORDER BY id DESC")
    public abstract List<Phone> loadAllPhones();

    @Query("SELECT * FROM Phone WHERE phone_number" +
            " LIKE '%' || :part_phone || '%' ORDER BY id DESC")
    public abstract List<Phone> loadPhonesByPartPhone(String part_phone);

    @Query("SELECT * FROM Phone WHERE client_id = :client_id ORDER BY id DESC")
    public abstract List<Phone> loadPhonesByClientId(int client_id);
}
