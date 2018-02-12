package pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;
import pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders.PhoneViewHolder;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneViewHolder> {

    private List<Phone> phones;
    private LinearLayoutManager mLayoutManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhoneAdapter(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        this.phones = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_phone_item, parent, false);
        // Set the view's size, margins, paddings and layout parameters
        return new PhoneViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        // - Get element from clients at this position
        // - Replace the contents of the view with that element
        holder.setPhone(phones.get(position));
    }

    /**
     * Scrolls to the top of the {@link List<Phone>}
     */
    public void scrollToTop() {
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * Use this method to update the {@link List<Phone>} to be shown to the user
     *
     * @param newPhones
     */
    public void setPhone(List<Phone> newPhones) {
        PhoneNumbersDiffUtil phoneNumbersDiffUtil =
                new PhoneNumbersDiffUtil(phones, newPhones);
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(phoneNumbersDiffUtil);
        phones.clear();
        phones.addAll(newPhones);
        diffResult.dispatchUpdatesTo(this);
    }

    // Return the size of your clients list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return phones.size();
    }

    public class PhoneNumbersDiffUtil extends DiffUtil.Callback {

        private List<Phone> oldList, newList;

        public PhoneNumbersDiffUtil(List<Phone> oldList, List<Phone> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return oldList.get(i).getPhoneNumber() == newList.get(i1).getPhoneNumber();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldList.get(i).equals(newList.get(i1));
        }
    }
}
