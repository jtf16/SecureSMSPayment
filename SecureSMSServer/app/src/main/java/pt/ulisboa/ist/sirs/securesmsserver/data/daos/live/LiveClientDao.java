package pt.ulisboa.ist.sirs.securesmsserver.data.daos.live;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;

@Dao
public abstract class LiveClientDao {

    @Query("SELECT * FROM Client ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadAllClients();

    @Query("SELECT * FROM Client WHERE IBAN LIKE '%' || :partIBAN || '%' ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsByPartIBAN(String partIBAN);

    @Query("SELECT * FROM Client WHERE balance >= :balance ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBalanceGreaterOrEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance > :balance ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBalanceGreaterThan(float balance);

    @Query("SELECT * FROM Client WHERE balance <= :balance ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBalanceLowerOrEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance < :balance ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBalanceLowerThan(float balance);

    @Query("SELECT * FROM Client WHERE balance = :balance ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBalanceEqualTo(float balance);

    @Query("SELECT * FROM Client WHERE balance BETWEEN :minBalance AND :maxBalance " +
            "ORDER BY id DESC")
    public abstract LiveData<List<Client>> loadClientsBetweenBalances(
            float minBalance, float maxBalance);
}
