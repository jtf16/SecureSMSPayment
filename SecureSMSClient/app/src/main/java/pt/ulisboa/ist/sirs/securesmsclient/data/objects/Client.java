package pt.ulisboa.ist.sirs.securesmsclient.data.objects;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "Client",
        indices = {@Index(value = {"IBAN"}, unique = true)})
public class Client {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int uid;

    @ColumnInfo(name = "IBAN")
    private String IBAN;

    @ColumnInfo(name = "balance")
    private float balance;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIBAN() {
        return this.IBAN;
    }

    public String getIBANWithSpaces() {
        return insertSpaceIntoIBAN(IBAN, " ", 4);
    }

    public void setIBAN(String IBAN) {
        this.IBAN = StringUtils.deleteWhitespace(IBAN);
    }

    public float getBalance() {
        return this.balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    private String insertSpaceIntoIBAN(String text, String insert, int period) {
        Pattern p = Pattern.compile("(.{" + period + "})", Pattern.DOTALL);
        Matcher m = p.matcher(text);
        return m.replaceAll("$1" + insert);
    }
}
