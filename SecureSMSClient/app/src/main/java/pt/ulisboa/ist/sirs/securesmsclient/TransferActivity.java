package pt.ulisboa.ist.sirs.securesmsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TransferActivity extends AppCompatActivity {

    private EditText editTextIBAN;
    private EditText editTextAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        setEditTextIBAN();
        setEditTextAmount();
    }

    private void setEditTextIBAN() {
        editTextIBAN = (EditText) findViewById(R.id.iban);
    }

    private void setEditTextAmount() {
        editTextAmount = (EditText) findViewById(R.id.amount);
    }

    public void doTransfer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
