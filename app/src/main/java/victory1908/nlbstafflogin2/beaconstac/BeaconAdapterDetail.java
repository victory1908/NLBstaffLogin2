package victory1908.nlbstafflogin2.beaconstac;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import victory1908.nlbstafflogin2.MainActivity;
import victory1908.nlbstafflogin2.ManageBeaconFragment.FragmentEditBeaconDetail;
import victory1908.nlbstafflogin2.R;


public class BeaconAdapterDetail extends RecyclerView.Adapter<BeaconAdapterDetail.ViewHolder> {

    private Context context;
    List<Beacon> beacons;

    public BeaconAdapterDetail(Context context, List<Beacon> beacons) {
        this.context = context;
        this.beacons = beacons;
    }


    @Override
    public BeaconAdapterDetail.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View beaconView = inflater.inflate(R.layout.beacon_view_detail, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(beaconView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeaconAdapterDetail.ViewHolder holder, int position) {
        //Getting the particular item from the list
        final Beacon beacon = beacons.get(position);

        //Showing data on the views
        holder.BeaconName.setText(beacon.getBeaconName());
        holder.BeaconID.setText(beacon.getBeaconID());
        holder.BeaconSN.setText(beacon.getBeaconSN());
        holder.BeaconUUID.setText(beacon.getBeaconUUID());
        holder.BeaconMajor.setText(String.valueOf(beacon.getMajor()));
        holder.BeaconMinor.setText(String.valueOf(beacon.getMinor()));

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //View
        public TextView BeaconName;
        public TextView BeaconID;
        public TextView BeaconSN;
        public TextView BeaconUUID;
        public TextView BeaconMajor;
        public TextView BeaconMinor;
        public Button editBeacon;

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

            editBeacon = (Button)itemView.findViewById(R.id.editBeacon);
            editBeacon.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("beacon", beacons.get(getLayoutPosition()));
            Fragment fragment = new FragmentEditBeaconDetail();
            fragment.setArguments(bundle);
//
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentMainDrawer, fragment, "tag").addToBackStack(null).commit();
        }
    }
}

