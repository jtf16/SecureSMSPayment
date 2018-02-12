package pt.ulisboa.ist.sirs.securesmsserver.data.objects.main;

import java.util.Date;

public class Movement {

    private int uid;
    private int clientFrom;
    private int clientTo;
    private float amount;
    private Date date;
    private String state;

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
}
