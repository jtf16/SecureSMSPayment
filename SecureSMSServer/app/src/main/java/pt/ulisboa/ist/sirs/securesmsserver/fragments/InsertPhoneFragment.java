package pt.ulisboa.ist.sirs.securesmsserver.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pt.ulisboa.ist.sirs.securesmsserver.R;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link InsertPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertPhoneFragment extends DialogFragment {

    private EditText editTextIBAN;
    private EditText editTextPhone;
    private Button btnInsertPhone;

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

    /** Called when the user taps the Insert Phone button */
    public void doInsertPhone() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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
    }

    private void setEditTextPhone(View view) {
        editTextPhone = (EditText) view.findViewById(R.id.client_phoneNumber);
    }
}
