package victory1908.nlbstafflogin2.ManageEventBeaconFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterReAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterReAssign;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventAssigned extends BaseFragment implements View.OnClickListener {


    public ManageEventAssigned() {
        // Required empty public constructor
    }


    RequestQueue requestQueue;

//    Creating a List of event
    private List<Event> listEvents, eventSelected;
//    List<Event> listEvents;
    Event eventSelectedTest;
    private List<Beacon> listBeacons, beaconSelected;

    //Creating Views
    private RecyclerView recyclerView, beaconRecyclerView;
    private RecyclerView.LayoutManager layoutManager, layoutMangerBeacon;
    private RecyclerView.Adapter adapter, beaconAdapter;

    SwipeRefreshLayout swipeRefreshLayout,swipeRefreshLayoutBeacon;

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
//        adapter = new EventAdapterReAssignTest(getContext(),listEvents,eventSelectedTest);

        beaconAdapter = new BeaconAdapterReAssign(getContext(),listBeacons);

        recyclerView.setAdapter(adapter);
        beaconRecyclerView.setAdapter(beaconAdapter);

        requestQueue = CustomVolleyRequest.getInstance(this.getContext().getApplicationContext()).getRequestQueue();

        swipeRefreshLayout = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayoutBeacon = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayoutBeacon);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventRespond(requestQueue);
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayoutBeacon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBeaconRespond(requestQueue);
                if (swipeRefreshLayoutBeacon.isRefreshing()) swipeRefreshLayoutBeacon.setRefreshing(false);
            }
        });

        getEventRespond(requestQueue);

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

    private void getEventRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.GET_ALL_EVENT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            JSONArray eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            listEvents.clear();
                            listEvents.addAll(getEventDetail(eventArray));
                            adapter.notifyDataSetChanged();
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

    private void getBeaconRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.GET_ALL_BEACON_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            JSONArray beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");
                            listBeacons.clear();
                            listBeacons.addAll(getBeaconDetail(beaconArray));
                            beaconAdapter.notifyDataSetChanged();
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


    @Override
    public void onClick(View view) {
        if (view==editAssignBeacon) {
//            Toast.makeText(getContext(),adapter.getCheckItem().getEventTitle(),Toast.LENGTH_SHORT).show();
        }
    }
}
