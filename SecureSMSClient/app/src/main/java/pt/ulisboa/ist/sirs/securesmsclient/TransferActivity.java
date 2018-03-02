package pt.ulisboa.ist.sirs.securesmsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        editTextAmount.addTextChangedListener(new AmountTextWatcher());
    }

    public void doTransfer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class AmountTextWatcher implements TextWatcher {

        private static final int DIGITS_BEFORE_ZERO = 6;
        private static final int DIGITS_AFTER_ZERO = 2;
        private String mPreviousValue = "";
        private int mCursorPosition;
        private boolean mRestoringPreviousValueFlag = false;
        private Pattern patternWithDot = Pattern.compile("[0-9]{0," + (DIGITS_BEFORE_ZERO)
                + "}+((\\.[0-9]{0," + (DIGITS_AFTER_ZERO) + "})?)||(\\.)?");

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!mRestoringPreviousValueFlag) {
                mPreviousValue = charSequence.toString();
                mCursorPosition = editTextAmount.getSelectionStart();
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!mRestoringPreviousValueFlag) {
                if (!isValid(editable.toString())) {
                    mRestoringPreviousValueFlag = true;
                    restorePreviousValue();
                }
            } else {
                mRestoringPreviousValueFlag = false;
            }
        }

        private void restorePreviousValue() {
            editTextAmount.setText(mPreviousValue);
            editTextAmount.setSelection(mCursorPosition);
        }

        private boolean isValid(String s) {
            Matcher matcherDot = patternWithDot.matcher(s);
            return matcherDot.matches();
        }
    }
}
