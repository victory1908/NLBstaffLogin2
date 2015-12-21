package victory1908.nlbstafflogin2.beaconstac;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.event.Event;


public class BeaconAdapterAssign extends RecyclerView.Adapter<BeaconAdapterAssign.ViewHolder> {

    private Context context;
    List<Beacon> beacons, beaconSelected;

    public BeaconAdapterAssign(Context context, List<Beacon> beacons, List<Beacon> beaconSelected) {
        this.context = context;
        this.beacons = beacons;
        this.beaconSelected = beaconSelected;
    }


    @Override
    public BeaconAdapterAssign.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View beaconView = inflater.inflate(R.layout.beacon_view_assign, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(beaconView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeaconAdapterAssign.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Beacon beacon = beacons.get(position);

        //Showing data on the views
        holder.BeaconUUID.setText(beacon.getBeaconUUID());
        holder.BeaconMajor.setText(beacon.getBeaconMajor());
        holder.BeaconMinor.setText(beacon.getBeaconMinor());

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //View
        public EditText BeaconUUID;
        public EditText BeaconMajor;
        public EditText BeaconMinor;
        public CheckBox selectCheckBox;

        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range

            BeaconUUID = (EditText)itemView.findViewById(R.id.beaconUUID);
            BeaconMajor = (EditText)itemView.findViewById(R.id.beaconMajor);
            BeaconMinor = (EditText)itemView.findViewById(R.id.beaconMinor);

            selectCheckBox = (CheckBox)itemView.findViewById(R.id.beaconChecked);
            selectCheckBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked){
                beaconSelected.add(beacons.get(getLayoutPosition()));
            }else {
                if (beaconSelected.contains(beacons.get(getLayoutPosition())))
                    beaconSelected.remove(beacons.get(getLayoutPosition()));
            }
        }
    }
}

