package pt.ulisboa.ist.sirs.securesmsserver.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.data.repositories.TransactionRepository;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link InsertClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertClientFragment extends DialogFragment {

    private EditText editTextIBAN;
    private EditText editTextBalance;
    private EditText editTextPhone;
    private Button btnInsertClient;

    private TransactionRepository transactionRepository;

    public InsertClientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InsertClientFragment.
     */
    public static InsertClientFragment newInstance() {
        InsertClientFragment fragment = new InsertClientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /** Called when the user taps the Insert Client button */
    public void doInsertClient() {
        transactionRepository.insertClientAndPhones(getClient(), getPhone());

        dismiss();
    }

    private Client getClient() {
        Client client = null;

        if (editTextIBAN.getText().toString().length() > 0 &&
                editTextBalance.getText().toString().length() > 0) {
            String iban = editTextIBAN.getText().toString();
            float balance = Float.valueOf(editTextBalance.getText().toString());

            client = new Client();

            client.setIBAN(iban);
            client.setBalance(balance);
        }
        return client;
    }

    private Phone getPhone() {
        Phone phone = null;

        if (editTextPhone.getText().toString().length() > 0) {
            int phone_number = Integer.valueOf(editTextPhone.getText().toString());

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
                R.layout.fragment_insert_client, container, false);
        getDialog().setTitle("Add Client");

        setEditTextIBAN(rootView);

        setEditTextBalance(rootView);

        setEditTextPhone(rootView);

        setBtnInsertClient(rootView);

        return rootView;
    }

    private void setBtnInsertClient(View view) {
        btnInsertClient = (Button) view.findViewById(R.id.btn_insert_client);
        btnInsertClient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doInsertClient();
            }
        });
    }

    private void setEditTextBalance(View view) {
        editTextBalance = (EditText) view.findViewById(R.id.client_balance);
        editTextBalance.addTextChangedListener(new BalanceTextWatcher());
    }

    private void setEditTextIBAN(View view) {
        editTextIBAN = (EditText) view.findViewById(R.id.client_iban);
    }

    private void setEditTextPhone(View view) {
        editTextPhone = (EditText) view.findViewById(R.id.client_phoneNumber);
    }

    private class BalanceTextWatcher implements TextWatcher {

        private static final int DIGITS_AFTER_ZERO = 2;
        private String mPreviousValue = "";
        private int mCursorPosition;
        private boolean mRestoringPreviousValueFlag = false;
        private Pattern patternWithDot = Pattern.compile(
                "[0-9]*((\\.[0-9]{0," + DIGITS_AFTER_ZERO + "})?)||(\\.)?");

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!mRestoringPreviousValueFlag) {
                mPreviousValue = charSequence.toString();
                mCursorPosition = editTextBalance.getSelectionStart();
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
            editTextBalance.setText(mPreviousValue);
            editTextBalance.setSelection(mCursorPosition);
        }

        private boolean isValid(String s) {
            Matcher matcherDot = patternWithDot.matcher(s);
            return matcherDot.matches();
        }
    }
}
