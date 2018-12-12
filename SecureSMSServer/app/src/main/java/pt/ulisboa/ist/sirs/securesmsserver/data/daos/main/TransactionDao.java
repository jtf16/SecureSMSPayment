package pt.ulisboa.ist.sirs.securesmsserver.data.daos.main;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.smsops.SMSResponse;

@Dao
public abstract class TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertClients(Client... clients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPhones(Phone... phones);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMovements(Movement... movements);

    @Update
    public abstract void updateClients(Client... clients);

    /**
     * Here will be all the queries needed to perform the
     * methods with annotation @Transaction
     */
    @Query("SELECT * FROM Client where IBAN LIKE :IBAN LIMIT 1")
    public abstract Client loadClientByIBAN(String IBAN);

    @Query("SELECT Client.* FROM Phone INNER JOIN Client ON " +
            "Phone.client_id = Client.id WHERE phone_number LIKE :phoneNumber LIMIT 1")
    public abstract Client loadClientByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM Movement WHERE iv LIKE :iv LIMIT 1")
    public abstract Movement loadMovementByIv(String iv);

    /**
     * Here will be all transactions needed
     */
    @Transaction
    public void insertClientAndPhones(Client client, Phone... phones) {
        // Anything inside this method runs in a single transaction.
        if (client != null) {
            insertClients(client);
            if (phones != null) {
                for (Phone phone : phones) {
                    if (phone != null) {
                        phone.setClientId(loadClientByIBAN(client.getIBAN()).getUid());
                        insertPhones(phone);
                    }
                }
            }
        }
    }

    @Transaction
    public void insertPhonesByIBAN(String IBAN, Phone... phones) {
        // Anything inside this method runs in a single transaction.
        IBAN = StringUtils.deleteWhitespace(IBAN);
        Client client = loadClientByIBAN(IBAN);
        if (client != null && phones != null) {
            for (Phone phone : phones) {
                if (phone != null) {
                    phone.setClientId(client.getUid());
                    insertPhones(phone);
                }
            }
        }
    }

    @Transaction
    public int insertMovements(String IBANTo, String phoneFrom, String iv,
                               Movement... movements) {
        // Anything inside this method runs in a single transaction.
        Client clientTo = loadClientByIBAN(StringUtils.deleteWhitespace(IBANTo));
        Client clientFrom = loadClientByPhoneNumber(phoneFrom);
        Movement movementByIv = loadMovementByIv(iv);

        if (clientTo == null) {
            return SMSResponse.NONEXITENT_DESTINATION;
        } else if (clientFrom == null) {
            return SMSResponse.NONEXITENT_SENDER;
        } else if (movementByIv != null) {
            return SMSResponse.REPLAY_ATTACK;
        }
        if (movements != null) {
            // All parts needed in a movement exist

            for (Movement movement : movements) {
                if (movement != null) {
                    movement.setClientFrom(clientFrom.getUid());
                    movement.setClientTo(clientTo.getUid());
                    movement.setDate(new Date());
                    movement.setIv(iv);

                    if (clientFrom.getBalance() < movement.getAmount()) {
                        // Movement should not be processed
                        movement.setState("Not processed");
                        insertMovements(movement);
                        return SMSResponse.INSUFFICIENT_BALANCE;
                    }
                    // Movement should be processed
                    // Update sender and receiver clients balance
                    clientFrom.setBalance(clientFrom.getBalance() - movement.getAmount());
                    clientTo.setBalance(clientTo.getBalance() + movement.getAmount());
                    updateClients(clientFrom, clientTo);

                    movement.setState("Processed");
                    insertMovements(movement);
                }
            }
            return (int) (clientFrom.getBalance() * 100);
        }
        return SMSResponse.NO_MOVEMENTS;
    }
}
