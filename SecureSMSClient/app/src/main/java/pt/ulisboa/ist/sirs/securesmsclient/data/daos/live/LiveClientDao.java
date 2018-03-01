package pt.ulisboa.ist.sirs.securesmsclient.data.daos.live;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;

@Dao
public abstract class LiveClientDao {

    @Query("SELECT * FROM Client WHERE id = :id LIMIT 1")
    public abstract LiveData<Client> loadClientById(int id);

    @Query("SELECT * FROM Client WHERE IBAN LIKE :IBAN LIMIT 1")
    public abstract LiveData<Client> loadClientByIBAN(String IBAN);
}
