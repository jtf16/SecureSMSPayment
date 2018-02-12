package pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.secundary.ClientMovement;

public class MovementViewHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private TextView time;
    private TextView fromIBAN;
    private TextView toIBAN;
    private TextView amount;
    private TextView state;
    private ClientMovement clientMovement;

    public MovementViewHolder(View itemView) {
        super(itemView);
        // Define click listener for the ViewHolder's View.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MovementViewHolder.class.getName(),
                        "Element " + getAdapterPosition() + " clicked.");
            }
        });
        date = itemView.findViewById(R.id.item_transfer_date);
        time = itemView.findViewById(R.id.item_transfer_time);
        fromIBAN = itemView.findViewById(R.id.item_iban_from);
        toIBAN = itemView.findViewById(R.id.item_iban_to);
        amount = itemView.findViewById(R.id.item_transfer_amount);
        state = itemView.findViewById(R.id.item_state);
    }

    public void setMovementClient(ClientMovement clientMovement) {
        this.clientMovement = clientMovement;

        date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(
                clientMovement.getDate()));
        time.setText(DateFormat.getTimeInstance().format(
                clientMovement.getDate()));
        fromIBAN.setText(clientMovement.getClientFromIBAN());
        toIBAN.setText(clientMovement.getClientToIBAN());
        amount.setText(Float.toString(clientMovement.getAmount()));
        state.setText(clientMovement.getState());
    }
}
