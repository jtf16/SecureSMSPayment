package pt.ulisboa.ist.sirs.securesmsserver;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pt.ulisboa.ist.sirs.securesmsserver.fragments.ClientsFragment;
import pt.ulisboa.ist.sirs.securesmsserver.fragments.MovementsFragment;
import pt.ulisboa.ist.sirs.securesmsserver.fragments.PhonesFragment;

public class MainActivity extends AppCompatActivity implements
        ClientsFragment.OnClientsFragmentInteractionListener,
        MovementsFragment.OnMovementsFragmentInteractionListener,
        PhonesFragment.OnPhonesFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(
                this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClientsFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPhonesFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMovementsFragmentInteraction(Uri uri) {

    }
}
