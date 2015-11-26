//package com.mobstac.beaconstacexample;
package victory1908.nlbstafflogin2.beaconstac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import victory1908.nlbstafflogin2.R;


public class BeaconAdapter extends BaseAdapter {


    public static UUID beaconUUID;

    private ArrayList <MSBeacon> beacons;
    private Context ctx;
    private LayoutInflater myInflator;

    public BeaconAdapter(ArrayList<MSBeacon> arr, Context c) {
        super();
        beacons = arr;
        ctx = c;
        myInflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addBeacon(MSBeacon beacon) {
        if(!beacons.contains(beacon)) {
            beacons.add(beacon);
        }
    }

    public void removeBeacon(MSBeacon beacon) {
        if(beacons.contains(beacon)) {
            beacons.remove(beacon);
        }
    }

    public void clear() {
        beacons.clear();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public MSBeacon getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(beacons, new Comparator<MSBeacon>() {
            @Override
            public int compare(MSBeacon lhs, MSBeacon rhs) {
                if (lhs.getIsCampedOn())
                    return -1;
                else if (rhs.getIsCampedOn())
                    return 1;
                else
                    return 0;
            }
        });

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = myInflator.inflate(R.layout.beacon_view, parent, false);
        }
        MSBeacon beacon = beacons.get(position);

        TextView beaconName = (TextView) view.findViewById(R.id.device_name);
        beaconName.setText(beacon.getBeaconUUID().toString());

        TextView key = (TextView) view.findViewById(R.id.device_address);
        key.setText("Major: " + beacon.getMajor() + "\t\t\t Minor: " + beacon.getMinor() +
                " \t\t\t  Mean RSSI: " + beacon.getMeanRSSI());

        beaconUUID = beacon.getBeaconUUID();

        if (beacon.getIsCampedOn()) {
            view.setBackgroundResource(android.R.color.holo_green_light);
        } else {
            view.setBackgroundResource(android.R.color.background_light);
        }

        return view;

    }
}
