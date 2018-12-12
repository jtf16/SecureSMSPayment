package pt.ulisboa.ist.sirs.securesmsclient;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import org.iban4j.IbanFormat;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.ist.sirs.securesmsclient.security.SecurityManager;
import pt.ulisboa.ist.sirs.securesmsclient.smsops.Parser;
import pt.ulisboa.ist.sirs.securesmsclient.smsops.SMSSender;

public class TransferActivity extends AppCompatActivity {

    private static final int LOCK_REQUEST_CODE = 221;

    private static boolean validIBAN = false;
    private static boolean validAmount = false;
    private EditText editTextIBAN;
    private EditText editTextAmount;

    public static void setValidIBAN(boolean newValidIBAN) {
        validIBAN = newValidIBAN;
    }

    public static void setValidAmount(boolean newValidAmount) {
        validAmount = newValidAmount;
    }

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

    private void authenticateApp() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent i = keyguardManager.createConfirmDeviceCredentialIntent(
                    "Tranfer", "Authenticate yourself in order to do the transfer");
            try {
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {
                transferAction();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCK_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    transferAction();
                }
                break;
        }
    }

    public void doTransfer(View view) {

        if (validIBAN && validAmount) {

            checkForSmsPermission();

            validIBAN = false;
            validAmount = false;
        }
    }

    public void transferAction() {

        String iban = editTextIBAN.getText().toString();
        String amount = editTextAmount.getText().toString();

        SMSSender.sendMessage(Parser.parseIbanAndAmount(iban, amount), SecurityManager.SHARED_KEY);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Checks whether the app has SMS permission.
     */
    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an app-defined int constant.
            // The callback method gets the result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted.
            authenticateApp();
        }
    }

    /**
     * Processes permission request codes.
     *
     * @param requestCode  The request code passed in requestPermissions()
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MainActivity.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    authenticateApp();
                } else {
                    // Permission denied. Try later when it's needed
                    // MY_PERMISSIONS_REQUEST_SEND_SMS is an app-defined int constant.
                    // The callback method gets the result of the request.
                }
            }
        }
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

            if (editable.toString().equals("")) {
                setValidAmount(false);
            } else if (editable.toString().equals(".") ||
                    Float.valueOf(editable.toString()) == 0) {
                setValidAmount(false);
                // error message displayed to user
                editTextAmount.setError("Value must be greater than 0");
            } else {
                setValidAmount(true);
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

            if (s.length() > 0) {
                // IBAN validation
                try {
                    IbanUtil.validate(s.toString(), IbanFormat.Default);
                    // valid IBAN
                    setValidIBAN(true);
                } catch (IbanFormatException |
                        InvalidCheckDigitException |
                        UnsupportedCountryException e) {
                    // invalid
                    setValidIBAN(false);
                    // error message displayed to user
                    editTextIBAN.setError("Invalid IBAN");
                }
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
