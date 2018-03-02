package pt.ulisboa.ist.sirs.securesmsclient;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ulisboa.ist.sirs.securesmsclient.data.DatabaseCreator;
import pt.ulisboa.ist.sirs.securesmsclient.data.loaders.live.LiveClientLoader;
import pt.ulisboa.ist.sirs.securesmsclient.data.loaders.live.LiveMovementsByPartIBANLoader;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Client;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;
import pt.ulisboa.ist.sirs.securesmsclient.recyclerviews.adapters.MovementAdapter;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks {

    private TextView balanceTextView;

    private EditText editTextSearch;

    private RecyclerView mRecyclerView;
    private MovementAdapter movementAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final int MOVEMENTS_LOADER_ID = 0;
    private static final int CLIENT_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseCreator databaseCreator =
                new DatabaseCreator(getApplicationContext(), 3);

        setBalanceTexView();
        setEditTextSearch();
        setRecyclerView();

        // Prepare the loaders.  Either re-connect with an existing one,
        // or start a new one.
        getSupportLoaderManager().restartLoader(MOVEMENTS_LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(CLIENT_LOADER_ID, null, this);
    }

    /** Called when the user taps the Transfer button */
    public void goToTransfer(View view) {
        Intent intent = new Intent(this, TransferActivity.class);
        startActivity(intent);
    }

    private void setBalanceTexView() {
        balanceTextView = (TextView) findViewById(R.id.balance);
    }

    private void setEditTextSearch() {
        editTextSearch = (EditText) findViewById(R.id.iban_search);
        editTextSearch.setKeyListener(
                DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 "));
        editTextSearch.addTextChangedListener(new SearchTextWatcher(this));
    }

    private void setRecyclerView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.movements_list);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
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
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVEMENTS_LOADER_ID:
                return new LiveMovementsByPartIBANLoader(
                        this, editTextSearch.getText().toString());
            case CLIENT_LOADER_ID:
                return new LiveClientLoader(this, 0);
            default:
                return null;
        }
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
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {

            case MOVEMENTS_LOADER_ID:
                LiveData<List<Movement>> liveDataMovements = (LiveData<List<Movement>>) data;
                liveDataMovements.observe(this, new Observer<List<Movement>>() {
                    @Override
                    public void onChanged(@Nullable List<Movement> newMovements) {
                        movementAdapter.setMovements(newMovements);
                    }
                });
                break;
            case CLIENT_LOADER_ID:
                LiveData<Client> liveDataClient = (LiveData<Client>) data;
                liveDataClient.observe(this, new Observer<Client>() {
                    @Override
                    public void onChanged(@Nullable Client newClient) {
                        if (newClient != null) {
                            balanceTextView.setText(String.valueOf(newClient.getBalance()) + "€");
                        } else {
                            balanceTextView.setText("0.0€");
                        }
                    }
                });
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader loader) {
        switch (loader.getId()) {

            case MOVEMENTS_LOADER_ID:
                // Clear the data in the adapter.
                movementAdapter.setMovements(null);
                break;
        }
    }

    private class SearchTextWatcher implements TextWatcher {

        private MainActivity mainActivity;

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

        public SearchTextWatcher(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

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
        public void afterTextChanged(Editable s) {
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

                editTextSearch.setSelection(cursor);
            }
            getSupportLoaderManager().restartLoader(MOVEMENTS_LOADER_ID, null, mainActivity);
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
         * see the example in {@link SearchTextWatcher#isInputCorrect(String)}
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
