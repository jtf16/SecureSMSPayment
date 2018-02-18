package pt.ulisboa.ist.sirs.securesmsserver.data.daos.main;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import org.apache.commons.lang3.StringUtils;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Movement;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

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
            "Phone.client_id = Client.id WHERE phone_number = :phoneNumber LIMIT 1")
    public abstract Client loadClientByPhoneNumber(int phoneNumber);

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
        if (client != null  && phones != null) {
            for (Phone phone : phones) {
                if (phone != null) {
                    phone.setClientId(client.getUid());
                    insertPhones(phone);
                }
            }
        }
    }

    @Transaction
    public void insertMovements(String IBANTo, int phoneFrom, Movement... movements) {
        // Anything inside this method runs in a single transaction.
        IBANTo = StringUtils.deleteWhitespace(IBANTo);
        Client clientTo = loadClientByIBAN(IBANTo);
        Client clientFrom = loadClientByPhoneNumber(phoneFrom);

        if (clientTo != null && clientFrom != null && movements != null) {
            // All parts needed in a movement exist

            for (Movement movement : movements) {
                if (movement != null) {
                    movement.setClientFrom(clientFrom.getUid());
                    movement.setClientTo(clientTo.getUid());

                    if (clientFrom.getBalance() < movement.getAmount()) {
                        // Movement should not be processed
                        movement.setState("Not processed");
                    }
                    else {
                        // Movement should be processed
                        // Update sender and receiver clients balance
                        clientFrom.setBalance(clientFrom.getBalance() - movement.getAmount());
                        clientTo.setBalance(clientTo.getBalance() + movement.getAmount());
                        updateClients(clientFrom, clientTo);
                        movement.setState("Processed");
                    }
                    insertMovements(movement);
                }
            }
        }
    }
}
