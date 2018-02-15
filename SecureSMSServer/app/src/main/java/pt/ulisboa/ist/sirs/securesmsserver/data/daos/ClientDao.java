package pt.ulisboa.ist.sirs.securesmsserver.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;

@Dao
public abstract class ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertClients(Client... clients);

    @Update
    public abstract void updateClients(Client... clients);

    @Delete
    public abstract void deleteClients(Client... clients);

    @Query("SELECT COUNT(*) FROM Client")
    public abstract int countClients();

    @Query("SELECT * FROM Client WHERE id = :id LIMIT 1")
    public abstract Client loadClientById(int id);

    @Query("SELECT * FROM Client WHERE IBAN LIKE :IBAN LIMIT 1")
    public abstract Client loadClientByIBAN(String IBAN);

    @Query("SELECT * FROM Client ORDER BY id DESC")
    public abstract List<Client> loadAllClients();

    @Query("SELECT * FROM Client WHERE IBAN LIKE '%' || :partIBAN || '%' ORDER BY id DESC")
    public abstract List<Client> loadClientsByPartIBAN(String partIBAN);

    @Query("SELECT * FROM Client WHERE balance >= :balance ORDER BY id DESC")
    public abstract List<Client> loadClientsBalanceGreaterOrEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance > :balance ORDER BY id DESC")
    public abstract List<Client> loadClientsBalanceGreaterThan(float balance);

    @Query("SELECT * FROM Client WHERE balance <= :balance ORDER BY id DESC")
    public abstract List<Client> loadClientsBalanceLowerOrEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance < :balance ORDER BY id DESC")
    public abstract List<Client> loadClientsBalanceLowerThan(float balance);

    @Query("SELECT * FROM Client WHERE balance = :balance ORDER BY id DESC")
    public abstract List<Client> loadClientsBalanceEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance BETWEEN :minBalance AND :maxBalance " +
            "ORDER BY id DESC")
    public abstract List<Client> loadClientsBetweenBalances(float minBalance, float maxBalance);
}
