package pt.ulisboa.ist.sirs.securesmsclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;
import pt.ulisboa.ist.sirs.securesmsclient.recyclerviews.adapters.MovementAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovementAdapter movementAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRecyclerView();
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
}
