package pt.ulisboa.ist.sirs.securesmsclient.recyclerviews.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.ist.sirs.securesmsclient.R;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;
import pt.ulisboa.ist.sirs.securesmsclient.recyclerviews.viewholders.MovementViewHolder;

public class MovementAdapter extends RecyclerView.Adapter<MovementViewHolder> {

    private List<Movement> movements;
    private LinearLayoutManager mLayoutManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovementAdapter(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        this.movements = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movement_item, parent, false);
        // Set the view's size, margins, paddings and layout parameters
        return new MovementViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MovementViewHolder holder, int position) {
        // - Get element from listTransactions at this position
        // - Replace the contents of the view with that element
        holder.setMovement(movements.get(position));
    }

    /**
     * Scrolls to the top of the {@link List<Movement>}
     */
    public void scrollToTop() {
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * Use this method to update the {@link List<Movement>} to be shown to the user
     *
     * @param newMovements
     */
    public void setMovements(List<Movement> newMovements) {
        MovementsDiffUtil movementsDiffUtil =
                new MovementsDiffUtil(movements, newMovements);
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(movementsDiffUtil);
        movements.clear();
        movements.addAll(newMovements);
        diffResult.dispatchUpdatesTo(this);
    }

    // Return the size of your movements list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movements.size();
    }

    public class MovementsDiffUtil extends DiffUtil.Callback {

        private List<Movement> oldList, newList;

        public MovementsDiffUtil(List<Movement> oldList, List<Movement> newList) {
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
            return oldList.get(i).getUid() == newList.get(i1).getUid();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldList.get(i).equals(newList.get(i1));
        }
    }
}
