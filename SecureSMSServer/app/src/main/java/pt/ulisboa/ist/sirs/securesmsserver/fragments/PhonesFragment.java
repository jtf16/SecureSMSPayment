package pt.ulisboa.ist.sirs.securesmsserver.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.adapters.PhoneAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhonesFragment.OnPhonesFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhonesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PhoneAdapter phoneAdapter;
    private LinearLayoutManager mLayoutManager;

    private FloatingActionButton fab_add_phone;

    private OnPhonesFragmentInteractionListener mListener;

    public PhonesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhonesFragment.
     */
    public static PhonesFragment newInstance() {
        PhonesFragment fragment = new PhonesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
                R.layout.fragment_phones, container, false);

        setRecyclerView(rootView);

        setFab(rootView);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPhonesFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhonesFragmentInteractionListener) {
            mListener = (OnPhonesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhonesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setFab(View view) {
        fab_add_phone = (FloatingActionButton) view.findViewById(R.id.fab_add_phone);
        fab_add_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                InsertPhoneFragment insertPhoneFragment =
                        InsertPhoneFragment.newInstance();
                insertPhoneFragment.show(fm, "fragment_insert_phone");
            }
        });
    }

    private void setRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.phones_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        phoneAdapter = new PhoneAdapter(mLayoutManager);
        mRecyclerView.setAdapter(phoneAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPhonesFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPhonesFragmentInteraction(Uri uri);
    }
}
