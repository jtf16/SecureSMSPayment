package pt.ulisboa.ist.sirs.securesmsserver.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.iban4j.IbanFormat;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.data.repositories.TransactionRepository;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link InsertPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertPhoneFragment extends DialogFragment {

    private EditText editTextIBAN;
    private EditText editTextPhone;
    private Button btnInsertPhone;

    private boolean validIBAN = false;

    private TransactionRepository transactionRepository;

    public InsertPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertPhoneFragment.
     */
    public static InsertPhoneFragment newInstance() {
        InsertPhoneFragment fragment = new InsertPhoneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the user taps the Insert Phone button
     */
    public void doInsertPhone() {

        if (validIBAN && editTextPhone.length() > 0) {
            transactionRepository.insertPhonesByIBAN(
                    editTextIBAN.getText().toString(), getPhone());

            dismiss();
        }
    }

    private Phone getPhone() {
        Phone phone = null;

        if (editTextPhone.getText().toString().length() > 0) {
            String phone_number = editTextPhone.getText().toString();

            phone = new Phone();

            phone.setPhoneNumber(phone_number);
        }
        return phone;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        transactionRepository = new TransactionRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.fragment_insert_phone, container, false);
        getDialog().setTitle("Add Phone");

        setEditTextIBAN(rootView);

        setEditTextPhone(rootView);

        setBtnInsertPhone(rootView);

        return rootView;
    }

    private void setBtnInsertPhone(View view) {
        btnInsertPhone = (Button) view.findViewById(R.id.btn_insert_phone);
        btnInsertPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doInsertPhone();
            }
        });
    }

    private void setEditTextIBAN(View view) {
        editTextIBAN = (EditText) view.findViewById(R.id.client_iban);
        editTextIBAN.setKeyListener(
                DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 "));
        editTextIBAN.addTextChangedListener(new IBANTextWatcher());
    }

    private void setEditTextPhone(View view) {
        editTextPhone = (EditText) view.findViewById(R.id.client_phoneNumber);
    }

    public void setValidIBAN(boolean newValidIBAN) {
        validIBAN = newValidIBAN;
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
