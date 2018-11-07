package pt.ulisboa.ist.sirs.securesmsclient.data.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "Movement")
public class Movement {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int uid;

    @ColumnInfo(name = "from_or_to")
    private int fromOrTo;

    @ColumnInfo(name = "IBAN")
    private String IBAN;

    @ColumnInfo(name = "amount")
    private float amount;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "state")
    private String state;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFromOrTo() {
        return fromOrTo;
    }

    public void setFromOrTo(int fromOrTo) {
        this.fromOrTo = fromOrTo;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = StringUtils.deleteWhitespace(IBAN);
    }

    public String getIBANWithSpaces() {
        return insertSpaceIntoIBAN(IBAN, " ", 4);
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

    private String insertSpaceIntoIBAN(String text, String insert, int period) {
        Pattern p = Pattern.compile("(.{" + period + "})", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        return m.replaceAll("$1" + insert);
    }
}