package victory1908.nlbstafflogin2.ManageEventBeaconFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterAssign;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterReAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterEdit;
import victory1908.nlbstafflogin2.event.EventAdapterReAssign;
import victory1908.nlbstafflogin2.event.EventAdapterReAssignTest;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventAssigned extends Fragment implements View.OnClickListener {


    public ManageEventAssigned() {
        // Required empty public constructor
    }


    RequestQueue requestQueue;
    //JSON Array
    private JSONArray eventArray,beaconArray;

//    Creating a List of event
    private List<Event> listEvents, eventSelected;
//    List<Event> listEvents;
//    Event eventSelected;
    private List<Beacon> listBeacons, beaconSelected;

    //Creating Views
    private RecyclerView recyclerView, beaconRecyclerView;
    private RecyclerView.LayoutManager layoutManager, layoutMangerBeacon;
    private RecyclerView.Adapter adapter, beaconAdapter;

    ProgressBar progressBar;

    Button editAssignBeacon, editAssignEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_manage_event_assigned, container, false);

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);

        //Initializing Views
        recyclerView = (RecyclerView)viewFragment.findViewById(R.id.selectEvent);

        beaconRecyclerView = (RecyclerView)viewFragment.findViewById(R.id.selectBeacon);

        editAssignBeacon = (Button)viewFragment.findViewById(R.id.editAssignBeacon);
//        editAssignEvent = (Button)viewFragment.findViewById(R.id.editEventAssigned);

        //Initializing our listEvents list
        listEvents = new ArrayList();
        listBeacons = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        beaconRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        layoutMangerBeacon = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        beaconRecyclerView.setLayoutManager(layoutMangerBeacon);

        adapter = new EventAdapterReAssign(getContext(),listEvents);
//        adapter = new EventAdapterReAssignTest(getContext(),listEvents,eventSelected);

        beaconAdapter = new BeaconAdapterReAssign(getContext(),listBeacons);

        recyclerView.setAdapter(adapter);
        beaconRecyclerView.setAdapter(beaconAdapter);

        requestQueue = Volley.newRequestQueue(getActivity());

        getEventDetailRespond(requestQueue);

        getBeaconRespond(requestQueue);

        // Inflate the layout for this fragment
        return viewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
//        Toast.makeText(getContext(),"test2",Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    public void onPause() {
//        Toast.makeText(getContext(),"test2",Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    private void getEventDetailRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.GET_ALL_EVENT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            getEventDetail(eventArray);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void getEventDetail(JSONArray j) {
        listEvents.clear();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                Event event = new Event();
                JSONObject json = j.getJSONObject(i);

                event.setEventID(json.getString(Config.EVENT_ID));
                event.setEventTitle(json.getString(Config.EVENT_TITLE));
                event.setEventDesc(json.getString(Config.EVENT_DESC));
                event.setEventStartTime(json.getString(Config.EVENT_START_TIME));
                event.setEventEndTime(json.getString(Config.EVENT_END_TIME));

                //Adding the event object to the list
                listEvents.add(event);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    private void getBeaconRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.GET_ALL_BEACON_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");
                            getBeaconDetail(beaconArray);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void getBeaconDetail(JSONArray j) {
        listBeacons.clear();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                Beacon beacon = new Beacon();
                JSONObject json = j.getJSONObject(i);

                beacon.setBeaconName(json.getString(Config.BEACON_NAME));
                beacon.setBeaconID(json.getString(Config.BEACON_ID));
                beacon.setBeaconSN(json.getString(Config.BEACON_SN));
                beacon.setBeaconUUID(json.getString(Config.BEACON_UUID));
                beacon.setMajor(Integer.valueOf(json.getString(Config.BEACON_MAJOR)));
                beacon.setMinor(Integer.valueOf(json.getString(Config.BEACON_MINOR)));

                //Adding the event object to the list
                listBeacons.add(beacon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Notifying the adapter that data has been added or changed
        beaconAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        if (view==editAssignBeacon) {
//            Toast.makeText(getContext(),adapter.getCheckItem().getEventTitle(),Toast.LENGTH_SHORT).show();
        }
    }
}
