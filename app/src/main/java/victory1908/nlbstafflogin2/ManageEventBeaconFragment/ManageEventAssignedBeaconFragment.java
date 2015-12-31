package victory1908.nlbstafflogin2.ManageEventBeaconFragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterEventReAssign;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterEventUnAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;


public class ManageEventAssignedBeaconFragment extends BaseFragment {
    public ManageEventAssignedBeaconFragment() {
    }

    Event event;
//    Beacon beacon;
    TextView eventTitle;
    TextView eventDesc;
    TextView eventStartTime;
    TextView eventEndTime;
    TextView eventID;
    ProgressBar progressBar;

    List<Beacon> listBeacons, beaconAssigned,beaconAssignedSelected;
    List<Beacon> beaconAvailable, beaconAvailableSelected;

    RequestQueue requestQueue;

    RecyclerView assignedBeaconRecycleView,availableBeaconRecycleView;
    RecyclerView.Adapter assignedBeaconAdapter,availableBeaconAdapter;
    RecyclerView.LayoutManager layoutAssignedBeacon,layoutAvailableBeacon;

    SwipeRefreshLayout swipeRefreshLayout,swipeRefreshLayoutBeacon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.activity_manage_event_assigned, container, false);

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

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = this.getArguments();
        event = bundle.getParcelable("event");
        eventTitle = (TextView)viewFragment.findViewById(R.id.EventTitle);
        eventTitle.setText(event.getEventTitle());

        eventDesc = (TextView)viewFragment.findViewById(R.id.EventDesc);
        eventDesc.setText(event.getEventDesc());

        eventStartTime = (TextView)viewFragment.findViewById(R.id.EventStartTime);
        eventStartTime.setText(event.getEventStartTime());


        eventEndTime = (TextView)viewFragment.findViewById(R.id.EventEndTime);
        eventEndTime.setText(event.getEventEndTime());

        eventID = (TextView)viewFragment.findViewById(R.id.EventID);
        eventID.setText(event.getEventID());

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);

        requestQueue = CustomVolleyRequest.getInstance(this.getContext().getApplicationContext()).getRequestQueue();

        listBeacons = new ArrayList<>();
        beaconAssigned = new ArrayList<>();
        beaconAssignedSelected = new ArrayList<>();
        beaconAvailable = new ArrayList<>();
        beaconAvailableSelected = new ArrayList<>();

        assignedBeaconRecycleView = (RecyclerView)viewFragment.findViewById(R.id.beaconAssignedRecycleView);
        assignedBeaconRecycleView.setHasFixedSize(true);
        layoutAssignedBeacon = new LinearLayoutManager(getContext());
        assignedBeaconRecycleView.setLayoutManager(layoutAssignedBeacon);
        assignedBeaconAdapter = new BeaconAdapterEventUnAssign(getContext(),beaconAssigned, beaconAssignedSelected);
        assignedBeaconRecycleView.setAdapter(assignedBeaconAdapter);

        availableBeaconRecycleView = (RecyclerView)viewFragment.findViewById(R.id.beaconAvailable);
        availableBeaconRecycleView.setHasFixedSize(true);
        layoutAvailableBeacon = new LinearLayoutManager(getContext());
        availableBeaconRecycleView.setLayoutManager(layoutAvailableBeacon);
        availableBeaconAdapter = new BeaconAdapterEventReAssign(getContext(),listBeacons, beaconAvailableSelected);
        availableBeaconRecycleView.setAdapter(availableBeaconAdapter);


        swipeRefreshLayout = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayoutBeacon = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayoutBeacon);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(getBeaconFromEvent(event));
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayoutBeacon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(getAllBeaconRespond());
                if (swipeRefreshLayoutBeacon.isRefreshing())
                    swipeRefreshLayoutBeacon.setRefreshing(false);
            }
        });

        requestQueue.add(getBeaconFromEvent(event));
        requestQueue.add(getAllBeaconRespond());

        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        assignedBeaconAdapter.notifyDataSetChanged();

    }


    //getBeaconID from EventID
    private CustomJsonObjectRequest getBeaconFromEvent(Event event) {
        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);
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
                       JSONArray beaconArray = new JSONArray();
                       beaconArray = respond.getJSONArray("result");
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
                    Toast.makeText(getContext().getApplicationContext(), "Unable to fetch data event ID: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        return jsonObjectRequest;
    }

    //get All Beacon
    private CustomJsonObjectRequest getAllBeaconRespond() {

        progressBar.setVisibility(View.VISIBLE);
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, Config.GET_ALL_BEACON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            JSONArray beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");
                            listBeacons.clear();
                            listBeacons.addAll(getBeaconDetail(beaconArray));
                            listBeacons.removeAll(beaconAssigned);

                            for (int i = 0; i<beaconAssigned.size(); i++) {

                                Toast.makeText(getContext().getApplicationContext(),beaconAssigned.get(i).getBeaconID(),Toast.LENGTH_SHORT).show();

                            }

                            for (int j = 0; j <listBeacons.size() ; j++) {
                                Toast.makeText(getContext().getApplicationContext(),listBeacons.get(j).getBeaconID(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext().getApplicationContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return jsonObjectRequest;
    }

}
