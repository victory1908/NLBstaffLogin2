package victory1908.nlbstafflogin2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mobstac.beaconstac.models.MSBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterAssign;
import victory1908.nlbstafflogin2.event.EventAdapterEdit;

public class AssignBeacon extends AppCompatActivity {

    RequestQueue requestQueue;

    //JSON Array
    private JSONArray eventArray,beaconArray;

    //Creating a List of event
    private List<Event> listEvents, eventSelected;
    private List<Beacon> listBeacons, beaconSelected;


    //Creating Views
    private RecyclerView recyclerView, beaconRecyclerView;
    private RecyclerView.LayoutManager layoutManager, layoutMangerBeacon;
    private RecyclerView.Adapter adapter, beaconAdapter;


    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Initializing our listEvents list
        listEvents = new ArrayList();
        listBeacons = new ArrayList<>();

        listEvents.clear();
        listBeacons.clear();

        eventSelected = new ArrayList<>();
        beaconSelected = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.eventList);
        beaconRecyclerView = (RecyclerView) findViewById(R.id.beaconList);

        recyclerView.setHasFixedSize(true);
        beaconRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutMangerBeacon = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        beaconRecyclerView.setLayoutManager(layoutMangerBeacon);

        adapter = new EventAdapterAssign(this,listEvents,eventSelected);
        beaconAdapter = new BeaconAdapterAssign(this,listBeacons,beaconSelected);

        recyclerView.setAdapter(adapter);
        beaconRecyclerView.setAdapter(beaconAdapter);

        requestQueue = Volley.newRequestQueue(this);
        getEventDetailRespond(requestQueue);
        getBeaconRespond(requestQueue);

    }

    @Override
    protected void onResume() {
        listEvents.clear();
        listBeacons.clear();

        getEventDetailRespond(requestQueue);
        getBeaconRespond(requestQueue);

        adapter.notifyDataSetChanged();
        beaconAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        listEvents.clear();
        listBeacons.clear();

        getEventDetailRespond(requestQueue);
        adapter.notifyDataSetChanged();

        getBeaconRespond(requestQueue);
        beaconAdapter.notifyDataSetChanged();

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        listEvents.clear();
        listBeacons.clear();

        getEventDetailRespond(requestQueue);
        adapter.notifyDataSetChanged();

        getBeaconRespond(requestQueue);
        beaconAdapter.notifyDataSetChanged();
    }

    private void getEventDetailRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_ALL_URL,
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
                        Toast.makeText(AssignBeacon.this, "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_BEACON_URL,
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
                        Toast.makeText(AssignBeacon.this, "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
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

                beacon.setBeaconUUID(json.getString(Config.BEACON_UUID));
                beacon.setBeaconMajor(json.getString(Config.BEACON_MAJOR));
                beacon.setBeaconMinor(json.getString(Config.BEACON_MINOR));


                //Adding the event object to the list
                listBeacons.add(beacon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Notifying the adapter that data has been added or changed
        beaconAdapter.notifyDataSetChanged();
    }




    public void eventSelected (View view){
        for (int i = 0; i <eventSelected.size() ; i++) {
            Toast.makeText(this,eventSelected.get(i).getEventTitle().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void beaconSelected (View view){
        for (int i = 0; i <beaconSelected.size() ; i++) {
            Toast.makeText(this,beaconSelected.get(i).getBeaconMajor().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
