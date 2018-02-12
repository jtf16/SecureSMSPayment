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
 * Use the {@link InsertClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertClientFragment extends DialogFragment {

    private EditText editTextIBAN;
    private EditText editTextBalance;
    private EditText editTextPhone;
    private Button btnInsertClient;

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
    }

    private void setEditTextIBAN(View view) {
        editTextIBAN = (EditText) view.findViewById(R.id.client_iban);
    }

    private void setEditTextPhone(View view) {
        editTextPhone = (EditText) view.findViewById(R.id.client_phoneNumber);
    }
}