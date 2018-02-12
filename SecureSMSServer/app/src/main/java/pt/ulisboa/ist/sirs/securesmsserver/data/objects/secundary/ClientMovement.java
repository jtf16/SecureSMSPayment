package pt.ulisboa.ist.sirs.securesmsserver.data.objects.secundary;

import java.util.Date;

public class ClientMovement {
    private int id;
    private String clientFromIBAN;
    private String clientToIBAN;
    private float amount;
    private Date date;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientFromIBAN() {
        return clientFromIBAN;
    }

    public void setClientFromIBAN(String clientFromIBAN) {
        this.clientFromIBAN = clientFromIBAN;
    }

    public String getClientToIBAN() {
        return clientToIBAN;
    }

    public void setClientToIBAN(String clientToIBAN) {
        this.clientToIBAN = clientToIBAN;
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
