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
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;
import pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders.ClientViewHolder;

public class ClientAdapter extends RecyclerView.Adapter<ClientViewHolder> {

    private List<Client> clients;
    private LinearLayoutManager mLayoutManager;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ClientAdapter(LinearLayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
        this.clients = new ArrayList<>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_client_item, parent, false);
        // Set the view's size, margins, paddings and layout parameters
        return new ClientViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        // - Get element from clients at this position
        // - Replace the contents of the view with that element
        holder.setClient(clients.get(position));
    }

    /**
     * Scrolls to the top of the {@link List<Client>}
     */
    public void scrollToTop() {
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * Use this method to update the {@link List<Client>} to be shown to the user
     *
     * @param newClients
     */
    public void setClients(List<Client> newClients) {
        ClientsDiffUtil clientsDiffUtil =
                new ClientsDiffUtil(clients, newClients);
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(clientsDiffUtil);
        clients.clear();
        clients.addAll(newClients);
        diffResult.dispatchUpdatesTo(this);
    }

    // Return the size of your clients list (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return clients.size();
    }

    public class ClientsDiffUtil extends DiffUtil.Callback {

        private List<Client> oldList, newList;

        public ClientsDiffUtil(List<Client> oldList, List<Client> newList) {
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
