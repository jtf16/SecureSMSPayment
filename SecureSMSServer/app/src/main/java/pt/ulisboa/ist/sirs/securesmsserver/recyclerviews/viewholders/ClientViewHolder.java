package pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Client;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    private TextView IBAN;
    private TextView balance;
    private Client client;

    public ClientViewHolder(View itemView) {
        super(itemView);
        // Define click listener for the ViewHolder's View.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(ClientViewHolder.class.getName(),
                        "Element " + getAdapterPosition() + " clicked.");
            }
        });
        IBAN = itemView.findViewById(R.id.item_iban);
        balance = itemView.findViewById(R.id.item_balance);
    }

    public void setClient(Client client) {
        this.client = client;
        IBAN.setText(client.getIBANWithSpaces());
        balance.setText(Float.toString(client.getBalance()));
    }
}
