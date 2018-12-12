package pt.ulisboa.ist.sirs.securesmsserver.data.objects.main;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Movement",
        foreignKeys = {
                @ForeignKey(onDelete = CASCADE,
                        entity = Client.class, parentColumns = "id",
                        childColumns = "client_from_id"),
                @ForeignKey(onDelete = CASCADE,
                        entity = Client.class, parentColumns = "id",
                        childColumns = "client_to_id")})
public class Movement {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int uid;

    @ColumnInfo(name = "client_from_id")
    private int clientFrom;

    @ColumnInfo(name = "client_to_id")
    private int clientTo;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "iv")
    private String iv;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getClientFrom() {
        return clientFrom;
    }

    public void setClientFrom(int clientFrom) {
        this.clientFrom = clientFrom;
    }

    public int getClientTo() {
        return clientTo;
    }

    public void setClientTo(int clientTo) {
        this.clientTo = clientTo;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
