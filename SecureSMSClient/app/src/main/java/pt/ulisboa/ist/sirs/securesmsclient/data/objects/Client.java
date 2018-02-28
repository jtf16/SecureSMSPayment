package pt.ulisboa.ist.sirs.securesmsclient.data.objects;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private String IBAN;
    private float balance;

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
