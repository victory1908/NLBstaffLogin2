package victory1908.nlbstafflogin2.beaconstac;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import victory1908.nlbstafflogin2.R;


public class BeaconAdapterEventReAssign extends RecyclerView.Adapter<BeaconAdapterEventReAssign.ViewHolder> {

    private Context context;
    List<Beacon> beacons, beaconSelected;

    public BeaconAdapterEventReAssign(Context context, List<Beacon> beacons, List<Beacon> beaconSelected) {
        this.context = context;
        this.beacons = beacons;
        this.beaconSelected = beaconSelected;
    }


    @Override
    public BeaconAdapterEventReAssign.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View beaconView = inflater.inflate(R.layout.beacon_view_assigned_event_re_assign, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(beaconView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeaconAdapterEventReAssign.ViewHolder holder, int position) {
        //Getting the particular item from the list
        final Beacon beacon = beacons.get(position);

        //Showing data on the views
        holder.BeaconName.setText(beacon.getBeaconName());
        holder.BeaconID.setText(beacon.getBeaconID());
        holder.BeaconSN.setText(beacon.getBeaconSN());
        holder.BeaconUUID.setText(beacon.getBeaconUUID());
        holder.BeaconMajor.setText(String.valueOf(beacon.getMajor()));
        holder.BeaconMinor.setText(String.valueOf(beacon.getMinor()));

        //in some case, it will prevent unwanted situations;
        holder.selectCheckBox.setOnCheckedChangeListener(null);

        //if true, your check box will be selected, else unselected
        holder.selectCheckBox.setChecked(beacon.isSelected());

        holder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                beacon.setIsSelected(isChecked);
                if (isChecked) {
                    beaconSelected.add(beacon);
                } else {
                    beaconSelected.remove(beacon);
                }
            }
        });


    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //View

        public TextView BeaconName;
        public TextView BeaconID;
        public TextView BeaconSN;
        public TextView BeaconUUID;
        public TextView BeaconMajor;
        public TextView BeaconMinor;
        public CheckBox selectCheckBox;

        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range

            BeaconName = (TextView)itemView.findViewById(R.id.beaconName);
            BeaconID = (TextView)itemView.findViewById(R.id.beaconID);
            BeaconSN = (TextView)itemView.findViewById(R.id.beaconSN);
            BeaconUUID = (TextView)itemView.findViewById(R.id.beaconUUID);
            BeaconMajor = (TextView)itemView.findViewById(R.id.beaconMajor);
            BeaconMinor = (TextView)itemView.findViewById(R.id.beaconMinor);

            selectCheckBox = (CheckBox)itemView.findViewById(R.id.beaconReAssign);


//            //Getting the particular item from the list
//            final Beacon beacon = beacons.get(getLayoutPosition());
//
//            //in some case, it will prevent unwanted situations;
//            selectCheckBox.setOnCheckedChangeListener(null);
//
//            //if true, your check box will be selected, else unselected
//            selectCheckBox.setChecked(beacon.isSelected());
//
//            selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    beacon.setIsSelected(isChecked);
//                    if (isChecked) {
//                        beaconSelected.add(beacon);
//                    } else {
//                        beaconSelected.remove(beacon);
//                    }
//                }
//            });


        }
    }
}

