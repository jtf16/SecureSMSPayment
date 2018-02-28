package pt.ulisboa.ist.sirs.securesmsclient.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;

@Dao
public abstract class ClientDao {

    @Insert
    public abstract void insertClients(Client... clients);

    @Update
    public abstract void updateClients(Client... clients);

    @Delete
    public abstract void deleteClients(Client... clients);

    @Transaction
    public void deleteAndInsertInTransaction(Client oldClient, Client newClient) {
        // Anything inside this method runs in a single transaction.
        deleteClients(oldClient);
        insertClients(newClient);
    }

    @Query("SELECT COUNT(*) FROM Client")
    public abstract int countClients();

    @Query("SELECT * FROM Client WHERE id = :id LIMIT 1")
    public abstract Client loadClientById(int id);

    @Query("SELECT * FROM Client WHERE IBAN LIKE :IBAN LIMIT 1")
    public abstract Client loadClientByIBAN(String IBAN);
}
