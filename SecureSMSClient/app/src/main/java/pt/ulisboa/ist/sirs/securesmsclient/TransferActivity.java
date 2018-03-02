package pt.ulisboa.ist.sirs.securesmsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
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
        editTextIBAN.setKeyListener(
                DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 "));
        editTextIBAN.addTextChangedListener(new IBANTextWatcher());
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

    private class IBANTextWatcher implements TextWatcher {

        // means divider position is every 5th symbol
        private static final int DIVIDER_MODULO = 5;
        private static final int GROUP_SIZE = DIVIDER_MODULO - 1;
        private static final char DIVIDER = ' ';
        private static final String STRING_DIVIDER = " ";
        private String previousText = "";

        private int deleteLength;
        private int insertLength;
        private int start;

        private String regexIBAN = "(\\w{" + GROUP_SIZE + "}" + DIVIDER +
                ")*\\w{1," + GROUP_SIZE + "}";
        private Pattern patternIBAN = Pattern.compile(regexIBAN);

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            this.previousText = s.toString();
            this.deleteLength = count;
            this.insertLength = after;
            this.start = start;
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
            String originalString = s.toString();

            if (!previousText.equals(originalString) &&
                    !isInputCorrect(originalString)) {
                String newString = previousText.substring(0, start);
                int cursor = start;

                if (deleteLength > 0 && s.length() > 0 &&
                        (previousText.charAt(start) == DIVIDER ||
                                start == s.length())) {
                    newString = previousText.substring(0, start - 1);
                    --cursor;
                }

                if (insertLength > 0) {
                    newString += originalString.substring(start, start + insertLength);
                    newString = buildCorrectInput(newString);
                    cursor = newString.length();
                }

                newString += previousText.substring(start + deleteLength);
                s.replace(0, s.length(), buildCorrectInput(newString));

                editTextIBAN.setSelection(cursor);
            }
        }

        /**
         * Check if String has the white spaces in the correct positions, meaning
         * if we have the String "123456789" and there should exist a white space
         * every 4 characters then the correct String should be "1234 5678 9".
         *
         * @param s String to be evaluated
         * @return true if string s is written correctly
         */
        private boolean isInputCorrect(String s) {
            Matcher matcherDot = patternIBAN.matcher(s);
            return matcherDot.matches();
        }

        /**
         * Puts the white spaces in the correct positions,
         * see the example in {@link IBANTextWatcher#isInputCorrect(String)}
         * to understand the correct positions.
         *
         * @param s String to be corrected.
         * @return String corrected.
         */
        private String buildCorrectInput(String s) {
            StringBuilder sbs = new StringBuilder(
                    s.replaceAll(STRING_DIVIDER, ""));

            // Insert the divider in the correct positions
            for (int i = GROUP_SIZE; i < sbs.length(); i += DIVIDER_MODULO) {
                sbs.insert(i, DIVIDER);
            }

            return sbs.toString();
        }
    }
}
