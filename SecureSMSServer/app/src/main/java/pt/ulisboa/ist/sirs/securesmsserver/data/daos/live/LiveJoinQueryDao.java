package pt.ulisboa.ist.sirs.securesmsserver.data.daos.live;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.data.objects.secundary.ClientMovement;

@Dao
public abstract class LiveJoinQueryDao {

    @Query("SELECT Movement.*, client_from.IBAN AS clientFromIBAN, "
            + "client_to.IBAN AS clientToIBAN FROM Movement "
            + "INNER JOIN Client client_from ON Movement.client_from_id = client_from.id "
            + "INNER JOIN Client client_to ON Movement.client_to_id = client_to.id "
            + "ORDER BY Movement.id DESC")
    public abstract LiveData<List<ClientMovement>> loadAllClientMovements();
}
