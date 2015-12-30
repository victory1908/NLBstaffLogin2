package victory1908.nlbstafflogin2.ManageEventBeaconFragment;


import android.content.DialogInterface;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterAssign;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterAssign;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignEventToBeacon extends BaseFragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;


    public AssignEventToBeacon() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AssignEventToBeacon newInstance(String param1, String param2) {
        AssignEventToBeacon fragment = new AssignEventToBeacon();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RequestQueue requestQueue;

    //Creating a List of event
    private List<Event> listEvents, eventSelected;
    private List<Beacon> listBeacons, beaconSelected;


    //Creating Views
    private RecyclerView eventRecyclerView, beaconRecyclerView;
    private RecyclerView.LayoutManager eventLayoutManager, beaconLayoutManger;
    private RecyclerView.Adapter eventAdapter, beaconAdapter;

    SwipeRefreshLayout swipeRefreshLayout,swipeRefreshLayoutBeacon;


    ProgressBar progressBar;
    Button assignBeaconToEvent;
    Button eventSelectedButton;
    Button beaconSelectedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        View viewFragment = inflater.inflate(R.layout.fragment_assign_beacon_to_event, container, false);

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

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);
        assignBeaconToEvent = (Button)viewFragment.findViewById(R.id.assignEventBeacon);
        eventSelectedButton = (Button)viewFragment.findViewById(R.id.eventSelected);
        beaconSelectedButton = (Button)viewFragment.findViewById(R.id.beaconSelected);

        //Initializing Views
        eventRecyclerView = (RecyclerView)viewFragment.findViewById(R.id.eventList);

        beaconRecyclerView = (RecyclerView)viewFragment.findViewById(R.id.beaconList);

        //Initializing our listEvents list
        listEvents = new ArrayList();
        listBeacons = new ArrayList<>();

        eventSelected = new ArrayList<>();
        beaconSelected = new ArrayList<>();


        assignBeaconToEvent.setOnClickListener(this);
        eventSelectedButton.setOnClickListener(this);
        beaconSelectedButton.setOnClickListener(this);


        eventRecyclerView.setHasFixedSize(true);
        beaconRecyclerView.setHasFixedSize(true);

        eventLayoutManager = new LinearLayoutManager(getContext());
        beaconLayoutManger = new LinearLayoutManager(getContext());

        eventRecyclerView.setLayoutManager(eventLayoutManager);
        beaconRecyclerView.setLayoutManager(beaconLayoutManger);

        eventAdapter = new EventAdapterAssign(getContext(),listEvents,eventSelected);
        beaconAdapter = new BeaconAdapterAssign(getContext(),listBeacons,beaconSelected);

        eventRecyclerView.setAdapter(eventAdapter);
        beaconRecyclerView.setAdapter(beaconAdapter);

        requestQueue = CustomVolleyRequest.getInstance(this.getContext().getApplicationContext()).getRequestQueue();

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
        getEventRespond(requestQueue);
        getBeaconRespond(requestQueue);
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        listEvents.clear();
        listBeacons.clear();
        eventSelected.clear();
        beaconSelected.clear();

        getEventRespond(requestQueue);

        getBeaconRespond(requestQueue);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        eventSelected.clear();
        beaconSelected.clear();
        super.onPause();
    }

    private void getEventRespond(RequestQueue requestQueue) {
        progressBar.setVisibility(View.VISIBLE);
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, Config.GET_ALL_EVENT_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            JSONArray eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            listEvents.clear();
                            listEvents.addAll(getEventDetail(eventArray));
                            eventAdapter.notifyDataSetChanged();
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
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,Config.GET_ALL_BEACON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            progressBar.setVisibility(View.GONE);
                            JSONArray beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");
                            listBeacons.clear();
                            listBeacons.addAll(getBeaconDetail(beaconArray));
                            beaconAdapter.notifyDataSetChanged();
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

    public void eventSelected (){
        for (int i = 0; i <eventSelected.size() ; i++) {
            Toast.makeText(getContext(),eventSelected.get(i).getEventTitle(),Toast.LENGTH_SHORT).show();
        }
    }

    public void beaconSelected (){
        for (int i = 0; i <beaconSelected.size() ; i++) {
            Toast.makeText(getContext(),String.valueOf(beaconSelected.get(i).getMajor()),Toast.LENGTH_SHORT).show();
        }
    }


    public void assignConfirm() {
        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you sure you want to assign Events to Beacons?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        for (int i = 0; i < eventSelected.size(); i++) {
                            for (int j = 0; j < beaconSelected.size(); j++) {
                                assignEventToBeacon(eventSelected.get(i), beaconSelected.get(j));
                            }
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void assignEventToBeacon(final Event event, final Beacon beacon) {

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ASSIGN_BEACON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.EVENT_ID, event.getEventID());
                map.put(Config.BEACON_ID,beacon.getBeaconID());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eventSelected:
                eventSelected();
                break;
            case R.id.beaconSelected:
                beaconSelected();
                break;
            case R.id.assignEventBeacon:
                assignConfirm();
                break;
//            default:
//                Log.i(getTag(), "Unknown: " + view.getId());
//                break;
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }


}
