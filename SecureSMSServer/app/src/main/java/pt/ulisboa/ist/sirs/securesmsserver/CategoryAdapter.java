package pt.ulisboa.ist.sirs.securesmsserver;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pt.ulisboa.ist.sirs.securesmsserver.fragments.ClientsFragment;
import pt.ulisboa.ist.sirs.securesmsserver.fragments.MovementsFragment;
import pt.ulisboa.ist.sirs.securesmsserver.fragments.PhonesFragment;

/**
 * {@link CategoryAdapter} is a {@link FragmentPagerAdapter} that can provide the layout for
 * each list item based on a data source which is a list of objects.
 */
public class CategoryAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return ClientsFragment.newInstance();
        } else if (position == 1) {
            return MovementsFragment.newInstance();
        } else {
            return PhonesFragment.newInstance();
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Return the Fragment title associated with a specified position.
     *
     * @param position
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.clients);
        } else if (position == 1) {
            return mContext.getString(R.string.movements);
        } else {
            return mContext.getString(R.string.phones);
        }
    }
}
