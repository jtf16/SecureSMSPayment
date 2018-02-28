package pt.ulisboa.ist.sirs.securesmsclient.data.objects;

import java.util.Date;

public class Movement {

    private int uid;
    private int fromOrTo;
    private String IBAN;
    private float amount;
    private Date date;
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
        this.IBAN = IBAN;
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
}