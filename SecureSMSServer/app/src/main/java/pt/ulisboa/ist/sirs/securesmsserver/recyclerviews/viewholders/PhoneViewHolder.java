package pt.ulisboa.ist.sirs.securesmsserver.recyclerviews.viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import pt.ulisboa.ist.sirs.securesmsserver.R;
import pt.ulisboa.ist.sirs.securesmsserver.data.objects.main.Phone;

public class PhoneViewHolder extends RecyclerView.ViewHolder {
    private TextView phoneNumber;
    private Phone phone;

    public PhoneViewHolder(View itemView) {
        super(itemView);
        // Define click listener for the ViewHolder's View.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(PhoneViewHolder.class.getName(),
                        "Element " + getAdapterPosition() + " clicked.");
            }
        });
        phoneNumber = itemView.findViewById(R.id.item_phone);
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
        this.phoneNumber.setText(phone.getPhoneNumber());
    }
}
