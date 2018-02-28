package pt.ulisboa.ist.sirs.securesmsclient.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;

@Dao
public abstract class MovementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMovements(Movement... movements);

    @Update
    public abstract void updateMovements(Movement... movements);

    @Delete
    public abstract void deleteMovements(Movement... movements);

    @Query("SELECT COUNT(*) FROM Movement")
    public abstract int countMovements();

    @Query("SELECT * FROM Movement WHERE id = :id LIMIT 1")
    public abstract Movement loadMovementById(int id);

    @Query("SELECT * FROM Movement ORDER BY id DESC")
    public abstract List<Movement> loadAllMovements();

    @Query("SELECT * FROM Movement WHERE from_or_to = :fromOrTo ORDER BY id DESC")
    public abstract List<Movement> loadMovementsByFromOrTo(int fromOrTo);

    @Query("SELECT * FROM Movement WHERE IBAN LIKE '%' || :partIBAN || '%' ORDER BY id DESC")
    public abstract List<Movement> loadMovementsByPartIBAN(String partIBAN);

    @Query("SELECT * FROM Movement WHERE amount >= :amount ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAmountGreaterOrEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount > :amount ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAmountGreaterThan(float amount);

    @Query("SELECT * FROM Movement WHERE amount <= :amount ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAmountLowerOrEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount < :amount ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAmountLowerThan(float amount);

    @Query("SELECT * FROM Movement WHERE amount = :amount ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAmountEqualTo(float amount);

    @Query("SELECT * FROM Movement WHERE amount BETWEEN :minAmount AND :maxAmount " +
            "ORDER BY id DESC")
    public abstract List<Movement> loadMovementsBetweenAmounts(float minAmount, float maxAmount);

    @Query("SELECT * FROM Movement WHERE date <= :date ORDER BY id DESC")
    public abstract List<Movement> loadMovementsBeforeDateInclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date < :date ORDER BY id DESC")
    public abstract List<Movement> loadMovementsBeforeDateExclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date >= :date ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAfterDateInclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date > :date ORDER BY id DESC")
    public abstract List<Movement> loadMovementsAfterDateExclusive(Date date);

    @Query("SELECT * FROM Movement WHERE date BETWEEN :from AND :to " +
            "ORDER BY id DESC")
    public abstract List<Movement> loadMovementsBetweenDates(Date from, Date to);

    @Query("SELECT * FROM Movement WHERE state = :state ORDER BY id DESC")
    public abstract List<Movement> loadMovementsByState(String state);
}
