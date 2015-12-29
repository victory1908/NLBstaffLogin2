package victory1908.nlbstafflogin2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.ArrayMap;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSConstants;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.core.PlaceSyncReceiver;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;
import com.mobstac.beaconstac.models.MSCard;
import com.mobstac.beaconstac.models.MSMedia;
import com.mobstac.beaconstac.utils.MSException;
import com.mobstac.beaconstac.utils.MSLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapter;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapter;


public class Beacon_MainFragment extends BaseFragment implements View.OnClickListener {

    public Beacon_MainFragment(){
        //Constructor;
    }

    Button fetchEvent;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    private TextView textViewWelcome;

    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG = Beacon_MainFragment.class.getSimpleName();

    private ArrayList<MSBeacon> beacons = new ArrayList<>();
    MSBeacon beaconAction;
    Beacon beaconInRange;

    Event eventInAction;

    private BeaconAdapter beaconAdapter;

    private TextView bCount;
    private TextView testCamped;
    Beaconstac bstacInstance;

//    private BluetoothAdapter mBluetoothAdapter;
//    private static final int REQUEST_ENABLE_BT = 1;

    private boolean registered = false;
    private boolean isPopupVisible = false;


    private ArrayList<String> eventIDBeacon;


    //JSON Array
    private JSONArray eventArray,beaconArray;

    //Creating a List of event
    private List<Event> listEvents;
    private List<Beacon> listBeacons;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    Spinner beaconList;

    Button dailyCheckIn;
    String staffID;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.beacon_activity_main, container, false);

        //checkLogged In
        //Fetching StaffID from shared preferences
        //If we will get true
        if (!loggedIn) {
            //We will start the Beacon_Main Activity
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        //Initializing our listBeacons list
        listEvents = new ArrayList();
        listBeacons = new ArrayList<>();

        beaconInRange = new Beacon();
        requestQueue = Volley.newRequestQueue(getContext());

        //Initializing Views
        recyclerView = (RecyclerView)viewFragment.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventAdapter(getContext(), listEvents);
        recyclerView.setAdapter(adapter);

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);

        fetchEvent = (Button)viewFragment.findViewById(R.id.fetchEvent);
        fetchEvent.setOnClickListener(this);

        dailyCheckIn = (Button)viewFragment.findViewById(R.id.buttonCheckIN);

        beaconList = (Spinner)viewFragment.findViewById(R.id.beaconSpinner);

        bCount = (TextView)viewFragment.findViewById(R.id.beaconCount);
        testCamped = (TextView)viewFragment.findViewById(R.id.CampedView);


        staffID = sharedPreferences.getString(Config.STAFF_ID, "Not Available");

        dailyCheckIn.setOnClickListener(this);


        // set region parameters (UUID and unique region identifier)
        bstacInstance = Beaconstac.getInstance(getContext());
        bstacInstance.setRegionParams("F94DBB23-2266-7822-3782-57BEAC0952AC",
                "com.mobstac.beaconstac");
        bstacInstance.syncRules();

        try {
            bstacInstance.startRangingBeacons();
            bstacInstance.setIsAutoManageBluetooth(true);
        } catch (MSException e) {
            // handle for older devices
            TextView rangedView = (TextView)viewFragment.findViewById(R.id.RangedView);
            rangedView.setText(R.string.ble_not_supported);
            bCount.setVisibility(View.GONE);
            testCamped.setVisibility(View.GONE);
            e.printStackTrace();
        }

        if ((!beacons.isEmpty()) && (listEvents.isEmpty())){
            fetchEvent.setVisibility(View.VISIBLE);
        }else fetchEvent.setVisibility(View.GONE);



        return viewFragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        setContentView(R.layout.beacon_activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("NLBstaffAttedance");
//        toolbar.setLogo(R.drawable.nlblogo);


//        admin.setVisibility(View.VISIBLE);


//        // Use this check to determine whether BLE is supported on the device.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//        }
//
//        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
//        // BluetoothAdapter through BluetoothManager.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//            mBluetoothAdapter = mBluetoothManager.getAdapter();
//        }
//
//        // Checks if Bluetooth is supported on the device.
//        if (mBluetoothAdapter == null) {
//            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
//            Toast.makeText(this, "Unable to obtain a BluetoothAdapter", Toast.LENGTH_LONG).show();
//        } else {
//            if (!mBluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//            }
//        }

//        eventView.setVisibility(View.INVISIBLE);



//        // if location is enabled
//        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            bstacInstance.syncPlaces();
//
//            new PlaceSyncReceiver() {
//
//                @Override
//                public void onSuccess(Context context) {
//                    bstacInstance.enableGeofences(true);
//
//                    try {
//                        bstacInstance.startRangingBeacons();
//                    } catch (MSException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Context context) {
//                    MSLogger.error("Error syncing geofence");
//                }
//            };
//
//        } else {
//            // if location disabled, start ranging beacons
//            try {
//                bstacInstance.startRangingBeacons();
//            } catch (MSException e) {
//                // handle for older devices
//                TextView rangedView = (TextView) findViewById(R.id.RangedView);
//                rangedView.setText(R.string.ble_not_supported);
//                bCount.setVisibility(View.GONE);
//                testCamped.setVisibility(View.GONE);
//                e.printStackTrace();
//            }
//        }


//        checkIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Beacon_MainFragment.this, ActivityCheckIn.class);
//                intent.putExtra(Config.STAFF_ID, staffID);
//                intent.putExtra(Config.EVENT_ID, eventCheckIN);
//                intent.putExtra("event",event);
//                startActivity(intent);
//
//            }
//        });

        initList();
    }

    // end OnCreate
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onPause() {
        super.onPause();
        beaconAdapter.clear();
        beaconAdapter.notifyDataSetChanged();
        listEvents.clear();
        adapter.notifyDataSetChanged();
        bCount.setText("" + beacons.size());
        unregisterBroadcast();
        isPopupVisible = true;
        dailyCheckIn.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcast();
        initList();
        bCount.setText("" + beacons.size());
        isPopupVisible = false;

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();

        // stop scanning when the app closes
        if (bstacInstance != null) {
            try {
                bstacInstance.stopRangingBeacons();
            } catch (MSException e) {
                // handle for older devices
                e.printStackTrace();
            }
        }
        dailyCheckIn.setVisibility(View.GONE);
    }

    // Callback intent results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
        }
        if (bstacInstance != null) {
            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                e.printStackTrace();
            }
        }
    }


    //method display ListBeacon
    private void initList() {
        beaconAdapter = new BeaconAdapter(beacons,getContext());
        beaconList.setAdapter(beaconAdapter);
        registerBroadcast();
    }

    private void registerBroadcast() {
        if (!registered) {
            // register beaconstac receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RANGED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_CAMPED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RULE_TRIGGERED);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_REGION);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_REGION);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_GEOFENCE);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_GEOFENCE);
            getContext().registerReceiver(beaconstacReceiver, intentFilter);

            //register place sync receiver
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_SUCCESS);
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_FAILURE);
            getContext().registerReceiver(placeSyncReceiver, iFilter);

            registered = true;
        }
    }

    private void unregisterBroadcast() {
        if (registered) {
            // unregister beaconstac receiver
            getContext().unregisterReceiver(beaconstacReceiver);
            // unregister place sync receiver
            getContext().unregisterReceiver(placeSyncReceiver);
            registered = false;
        }
    }

    PlaceSyncReceiver placeSyncReceiver = new PlaceSyncReceiver() {

        @Override
        public void onSuccess(Context context) {
            bstacInstance.enableGeofences(true);

            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Context context) {
            MSLogger.error("Error syncing geofence");
        }
    };


    BeaconstacReceiver beaconstacReceiver = new BeaconstacReceiver() {
        @Override
        public void exitedBeacon(Context context, MSBeacon beacon) {
            testCamped.setText("Exited: " + beacon.getMajor() + ":" + beacon.getMinor());
            beaconAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            dailyCheckIn.setVisibility(View.GONE);
        }

        @Override
        public void rangedBeacons(Context context, ArrayList<MSBeacon> rangedBeacons) {
            beaconAdapter.clear();
            bCount.setText("" + rangedBeacons.size());
            beacons.addAll(rangedBeacons);
            beaconAdapter.notifyDataSetChanged();

            if (!beacons.isEmpty()) {

                beaconList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        beaconAction = beacons.get(position);
                        beaconInRange.setBeaconUUID(beaconAction.getBeaconUUID().toString());
                        beaconInRange.setMajor(beaconAction.getMajor());
                        beaconInRange.setMinor(beaconAction.getMinor());

                        recyclerView.setAdapter(adapter);

                        requestQueue.add(getBeaconIDInRange(beaconInRange));

                        dailyCheckIn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        listEvents.clear();
                        adapter.notifyDataSetChanged();
                        dailyCheckIn.setVisibility(View.VISIBLE);
                    }
                });
            }

        }

        @Override
        public void campedOnBeacon(Context context, MSBeacon beacon) {
            testCamped.setText("Camped: " + beacon.getMajor() + ":" + beacon.getMinor());
            beaconAdapter.addBeacon(beacon);
            beaconAdapter.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
        }

        @Override
        public void triggeredRule(Context context, String ruleName, ArrayList<MSAction> actions) {
            HashMap<String, Object> messageMap;
            AlertDialog.Builder dialogBuilder;

            if (!isPopupVisible) {
                for (MSAction action : actions) {

                    messageMap = action.getMessage();

                    switch (action.getType()) {
                        // handle action type Popup
                        case MSActionTypePopup:
                            dialogBuilder = new AlertDialog.Builder(context);
                            messageMap = action.getMessage();
                            dialogBuilder.setTitle(action.getName()).setMessage((String) messageMap.get("text"));
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    isPopupVisible = false;
                                }
                            });
                            dialog.show();
                            isPopupVisible = true;
                            break;

                        // handle the action type Card
                        case MSActionTypeCard:
                            MSCard card = (MSCard) messageMap.get("card");

                            switch (card.getType()) {
                                case MSCardTypePhoto:
                                case MSCardTypeMedia:
                                    ArrayList<MSMedia> mediaArray = card.getMediaArray();
                                    if (mediaArray.size() > 0) {
                                        MSMedia m = mediaArray.get(0);

                                        String src = m.getMediaUrl().toString();

                                        dialogBuilder = new AlertDialog.Builder(context);
                                        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                isPopupVisible = false;
                                            }

                                        });

                                        final WebView webView = new WebView(context);
                                        webView.loadUrl(src);

                                        dialogBuilder.setView(webView);
                                        dialogBuilder.setPositiveButton("Close", null);
                                        dialogBuilder.show();

                                        isPopupVisible = true;
                                    }
                            }
                            break;

                        // handle action type webpage
                        case MSActionTypeWebpage:
                            if (!isPopupVisible) {
                                dialogBuilder = new AlertDialog.Builder(context);
                                dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        isPopupVisible = false;
                                    }

                                });

                                final WebView webView = new WebView(context);
                                webView.loadUrl(messageMap.get("url").toString());

                                dialogBuilder.setView(webView);
                                dialogBuilder.setPositiveButton("Close", null);
                                dialogBuilder.show();

                                isPopupVisible = true;

                            }
                            break;
                    }
                }
                Toast.makeText(getContext(), "Rule " + ruleName, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void enteredRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getContext(), "Entered region", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();

            listEvents.clear();
            adapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getContext(), "Exited region", Toast.LENGTH_SHORT).show();

            dailyCheckIn.setVisibility(View.GONE);

        }

        @Override
        public void enteredGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getContext(), "Entered Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getContext(), "Exited Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
            dailyCheckIn.setVisibility(View.GONE);
        }
    };


    //getEventDetail
    private JsonObjectRequest getEventDetailRespond(Event event) {
//        Toast.makeText(getContext(),event.getEventID(),Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put(Config.EVENT_ID,event.getEventID());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_URL, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respond) {
                            try {
                                eventArray = new JSONArray();
                                eventArray = respond.getJSONArray("result");
                                getEventDetail(eventArray);
//                                Toast.makeText(getContext(),respond.toString(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " +error.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
//        //Adding request to the queue
//        requestQueue.add(jsonObjectRequest);
        return jsonObjectRequest;
    }

    private void getEventDetail(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                Event event = new Event();
                //Getting json object
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



    //getBeaconID from RangedBeacon
    private JsonObjectRequest getBeaconIDInRange(Beacon beacon) {

        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put(Config.BEACON_UUID, beacon.getBeaconUUID());
        jsonParams.put(Config.BEACON_MAJOR, beacon.getMajor());
        jsonParams.put(Config.BEACON_MINOR, beacon.getMinor());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.GET_BEACON_ID, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");
                            getBeaconID(beaconArray);

                            requestQueue.add(getEventIDFromBeacon(beaconInRange));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " +error.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return jsonObjectRequest;
    }

    private void getBeaconID(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                beaconInRange.setBeaconID(json.getString(Config.BEACON_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //getEventID from ranged beacon
    private JsonObjectRequest getEventIDFromBeacon(Beacon beacon) {

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);

        JSONObject params = new JSONObject();
        try {
            params.put(Config.BEACON_ID, beacon.getBeaconID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Creating a JSONObject request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.EVENT_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            getEventIDFromBeacon(eventArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event ID: " +error.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };
        return jsonObjectRequest;
    }

    private void getEventIDFromBeacon(JSONArray j) {
        listEvents.clear();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            eventInAction = new Event();
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the event to array list
                eventInAction.setEventID(json.getString(Config.EVENT_ID));
                requestQueue.add(getEventDetailRespond(eventInAction));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == fetchEvent) getEventIDFromBeacon(beaconInRange);
        if (view == dailyCheckIn) dailyCheckIn();
    }

    private void dailyCheckIn() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DAILY_CHECK_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.STAFF_ID, sharedPreferences.getString(Config.STAFF_ID, "Not Available"));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
