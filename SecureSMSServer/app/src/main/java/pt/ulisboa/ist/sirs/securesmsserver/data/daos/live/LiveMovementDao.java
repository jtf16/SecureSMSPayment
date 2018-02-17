package pt.ulisboa.ist.sirs.securesmsserver.data.daos.live;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;

@Dao
public abstract class LiveMovementDao {

    @Query("SELECT * FROM Movement ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadAllMovements();

    @Query("SELECT * FROM Movement WHERE client_from_id = :id ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsByClientFromId(int id);

    @Query("SELECT * FROM Movement WHERE client_to_id = :id ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsByClientToId(int id);

    @Query("SELECT * FROM Movement WHERE amount >= :amount ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAmountGreaterOrEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount > :amount ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAmountGreaterThan(float amount);

    @Query("SELECT * FROM Movement WHERE amount <= :amount ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAmountLowerOrEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount < :amount ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAmountLowerThan(float amount);

    @Query("SELECT * FROM Movement WHERE amount = :amount ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAmountEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount BETWEEN :minAmount AND :maxAmount " +
            "ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsBetweenAmounts(float minAmount, float maxAmount);

    @Query("SELECT * FROM Movement WHERE date <= :date ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsBeforeDateInclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date < :date ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsBeforeDateExclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date >= :date ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAfterDateInclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date > :date ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsAfterDateExclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date BETWEEN :from AND :to " +
            "ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsBetweenDates(Date from, Date to);

    @Query("SELECT * FROM Movement WHERE state = :state ORDER BY id DESC")
    public abstract LiveData<List<Movement>> loadMovementsByState(String state);
}
