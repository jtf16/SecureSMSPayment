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
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.secundary.ClientMovement;
import pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders.MovementViewHolder;

public class MovementAdapter extends RecyclerView.Adapter<MovementViewHolder> {

    private List<ClientMovement> clientMovements;
    private LinearLayoutManager mLayoutManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovementAdapter(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        this.clientMovements = new ArrayList<>();
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
        holder.setMovementClient(clientMovements.get(position));
    }

    /**
     * Scrolls to the top of the {@link List<ClientMovement>}
     */
    public void scrollToTop() {
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * Use this method to update the {@link List<ClientMovement>} to be shown to the user
     *
     * @param newMovements
     */
    public void setMovementClients(List<ClientMovement> newMovements) {
        ClientMovementsDiffUtil clientMovementsDiffUtil =
                new ClientMovementsDiffUtil(clientMovements, newMovements);
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(clientMovementsDiffUtil);
        clientMovements.clear();
        clientMovements.addAll(newMovements);
        diffResult.dispatchUpdatesTo(this);
    }

    // Return the size of your movements list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return clientMovements.size();
    }

    public class ClientMovementsDiffUtil extends DiffUtil.Callback {

        private List<ClientMovement> oldList, newList;

        public ClientMovementsDiffUtil(List<ClientMovement> oldList, List<ClientMovement> newList) {
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
            return oldList.get(i).getId() == newList.get(i1).getId();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldList.get(i).equals(newList.get(i1));
        }
    }
}
