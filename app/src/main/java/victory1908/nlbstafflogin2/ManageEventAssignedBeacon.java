package victory1908.nlbstafflogin2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterAssignedEvent;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterEventReAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;


public class ManageEventAssignedBeacon extends BaseActivity {

    Event event;
//    Beacon beacon;
    TextView eventTitle;
    TextView eventDesc;
    TextView eventStartTime;
    TextView eventEndTime;
    TextView eventID;
    ProgressBar progressBar;

    JSONArray beaconArray, beaconArrayDetail, beaconArrayAll;
    List<Beacon> listBeacons, beaconAssigned,beaconAssignedSelected;
    List<Beacon> beaconAvailable, beaconAvailableSelected;

    RequestQueue requestQueue, requestQueue1;

    RecyclerView assignedBeaconRecycleView,availableBeaconRecycleView;
    RecyclerView.Adapter assignedBeaconAdapter,availableBeaconAdapter;
    RecyclerView.LayoutManager layoutAssignedBeacon,layoutAvailableBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event_assigned);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = getIntent().getParcelableExtra("event");
        eventTitle = (TextView) (findViewById(R.id.EventTitle));
        eventTitle.setText(event.getEventTitle());

        eventDesc = (TextView) (findViewById(R.id.EventDesc));
        eventDesc.setText(event.getEventDesc());

        eventStartTime = (TextView) (findViewById(R.id.EventStartTime));
        eventStartTime.setText(event.getEventStartTime());


        eventEndTime = (TextView) (findViewById(R.id.EventEndTime));
        eventEndTime.setText(event.getEventEndTime());

        eventID = (TextView) (findViewById(R.id.EventID));
        eventID.setText(event.getEventID());

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        requestQueue = CustomVolleyRequest.getInstance(this.getApplicationContext()).getRequestQueue();
        requestQueue1 = Volley.newRequestQueue(this);

        listBeacons = new ArrayList<>();
        beaconAssigned = new ArrayList<>();
        beaconAssignedSelected = new ArrayList<>();
        beaconAvailable = new ArrayList<>();
        beaconAvailableSelected = new ArrayList<>();

        assignedBeaconRecycleView = (RecyclerView)findViewById(R.id.beaconAssignedRecycleView);
        assignedBeaconRecycleView.setHasFixedSize(true);
        layoutAssignedBeacon = new LinearLayoutManager(this);
        assignedBeaconRecycleView.setLayoutManager(layoutAssignedBeacon);
        assignedBeaconAdapter = new BeaconAdapterAssignedEvent(this,beaconAssigned, beaconAssignedSelected);
        assignedBeaconRecycleView.setAdapter(assignedBeaconAdapter);

        availableBeaconRecycleView = (RecyclerView)findViewById(R.id.beaconAvailable);
        availableBeaconRecycleView.setHasFixedSize(true);
        layoutAvailableBeacon = new LinearLayoutManager(this);
        availableBeaconRecycleView.setLayoutManager(layoutAvailableBeacon);
        availableBeaconAdapter = new BeaconAdapterEventReAssign(this,listBeacons, beaconAvailableSelected);
        availableBeaconRecycleView.setAdapter(availableBeaconAdapter);

        requestQueue.add(getBeaconFromEvent(event));

    }

    @Override
    protected void onResume() {
        super.onResume();
        assignedBeaconAdapter.notifyDataSetChanged();

    }


    //getBeaconID from EventID
    private CustomJsonObjectRequest getBeaconFromEvent(Event event) {
        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        JSONObject params = new JSONObject();
        try {
            params.put(Config.EVENT_ID, event.getEventID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Creating a JSONObject request
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,Config.GET_BEACON_FROM_EVENT, params,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject respond) {
                   try {
                       beaconArray = new JSONArray();
                       beaconArray = respond.getJSONArray("result");
                       beaconAssigned.clear();
                       beaconAssigned.addAll(getBeaconDetail(beaconArray));
                       //Notifying the adapter that data has been added or changed
                       assignedBeaconAdapter.notifyDataSetChanged();
                       requestQueue.add(getAllBeaconRespond());

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch data event ID: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        return jsonObjectRequest;
    }

    //get All Beacon
    private JsonObjectRequest getAllBeaconRespond() {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.GET_ALL_BEACON_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            beaconArrayAll = new JSONArray();
                            beaconArrayAll = respond.getJSONArray("result");
                            listBeacons.clear();
                            listBeacons.addAll(getBeaconDetail(beaconArrayAll));
                            listBeacons.removeAll(beaconAssigned);


                            for (int i = 0; i<beaconAssigned.size(); i++) {

                                Toast.makeText(getApplicationContext(),beaconAssigned.get(i).getBeaconID(),Toast.LENGTH_SHORT).show();

                            }

                            for (int j = 0; j <listBeacons.size() ; j++) {
                                Toast.makeText(getApplicationContext(),listBeacons.get(j).getBeaconID(),Toast.LENGTH_SHORT).show();
                            }



                            availableBeaconAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return jsonObjectRequest;
    }

}
