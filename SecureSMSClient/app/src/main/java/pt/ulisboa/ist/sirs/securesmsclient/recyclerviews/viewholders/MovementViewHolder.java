package pt.ulisboa.ist.sirs.securesmsclient.recyclerviews.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import pt.ulisboa.ist.sirs.securesmsclient.R;
import pt.ulisboa.ist.sirs.securesmsclient.data.objects.Movement;

public class MovementViewHolder extends RecyclerView.ViewHolder {
    private TextView fromOrTo;
    private TextView iban;
    private TextView amount;
    private TextView date;
    private TextView time;
    private Movement movement;

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
        fromOrTo = itemView.findViewById(R.id.item_from_or_to);
        iban = itemView.findViewById(R.id.item_iban);
        amount = itemView.findViewById(R.id.item_amount);
        date = itemView.findViewById(R.id.item_date);
        time = itemView.findViewById(R.id.item_time);
    }

    public void setMovement(Movement movement) {
        this.movement = movement;

        // - get element from listTransactions at this position
        // - replace the contents of the view with that element
        if (movement.getFromOrTo() == 0) {
            fromOrTo.setText(R.string.from);
        } else {
            fromOrTo.setText(R.string.to);
        }
        iban.setText(movement.getIBAN());
        amount.setText(movement.getAmount() + "€");
        Date realDate = movement.getDate();
        date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(realDate));
        time.setText(DateFormat.getTimeInstance().format(realDate));
    }
}
