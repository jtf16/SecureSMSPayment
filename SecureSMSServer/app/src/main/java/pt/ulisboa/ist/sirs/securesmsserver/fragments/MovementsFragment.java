package pt.ulisboa.ist.sirs.securesmsserver.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.loaders.live.LiveClientMovementsLoader;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.secundary.ClientMovement;
import pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.adapters.MovementAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovementsFragment.OnMovementsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovementsFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<LiveData<List<ClientMovement>>> {

    private RecyclerView mRecyclerView;
    private MovementAdapter movementAdapter;
    private LinearLayoutManager mLayoutManager;

    private OnMovementsFragmentInteractionListener mListener;

    public MovementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovementsFragment.
     */
    public static MovementsFragment newInstance() {
        MovementsFragment fragment = new MovementsFragment();
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
                R.layout.fragment_movements, container, false);

        setRecyclerView(rootView);

        // Prepare the loader.  Either reconnect with an existing one,
        // or start a new one.
        getLoaderManager().restartLoader(0, null, this);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMovementsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMovementsFragmentInteractionListener) {
            mListener = (OnMovementsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMovementsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movements_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        movementAdapter = new MovementAdapter(mLayoutManager);
        mRecyclerView.setAdapter(movementAdapter);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<LiveData<List<ClientMovement>>> onCreateLoader(int id, Bundle args) {
        return new LiveClientMovementsLoader(getActivity());
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<LiveData<List<ClientMovement>>> loader,
                               LiveData<List<ClientMovement>> data) {
        data.observe(this, new Observer<List<ClientMovement>>() {
            @Override
            public void onChanged(@Nullable List<ClientMovement> newMovementClients) {
                movementAdapter.setClientMovements(newMovementClients);
            }
        });
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<LiveData<List<ClientMovement>>> loader) {
        // Clear the data in the adapter.
        movementAdapter.setClientMovements(null);
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
    public interface OnMovementsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMovementsFragmentInteraction(Uri uri);
    }
}
