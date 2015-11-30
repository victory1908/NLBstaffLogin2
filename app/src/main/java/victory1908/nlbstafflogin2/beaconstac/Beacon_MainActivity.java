package victory1908.nlbstafflogin2.beaconstac;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

import victory1908.nlbstafflogin2.ActivityCheckIn;
import victory1908.nlbstafflogin2.BaseActivity;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.LoginActivity;
import victory1908.nlbstafflogin2.MainActivity;
import victory1908.nlbstafflogin2.R;


public class Beacon_MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private TextView textViewWelcome;

    private static final String TAG = Beacon_MainActivity.class.getSimpleName();

    private ArrayList<MSBeacon> beacons = new ArrayList<>();

    private BeaconAdapter beaconAdapter;
    private ArrayAdapter eventAdapter;

    private TextView bCount;
    private TextView testCamped;
    private Button checkIn;
    private RelativeLayout eventView;
    Beaconstac bstacInstance;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    private boolean registered = false;
    private boolean isPopupVisible = false;


    //Spinner
    //Declaring an Spinner
    public Spinner spinner;

    //An ArrayList for Spinner Items
    private ArrayList<String> eventDetail;
    private ArrayList <String> tempArray;
    private ArrayList <MSBeacon> tempBeacons;

    //JSON Array
    private JSONArray eventArray;

    //TextViews to display details
    private TextView textViewEventTitle;
    private TextView textViewEventDesc;
    private TextView textViewEventTime;
    private String staffID;
    private String eventCheckIN;
    private String beaconUUID;
    private int beaconMajor;
    private int beaconMinor;

    public int tempRangedBeacon = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLBstaffAttedance");
        toolbar.setLogo(R.drawable.nlblogo);
        textViewWelcome = (TextView) findViewById(R.id.textView_staffid);

        // initiate eventView;
        eventView = (RelativeLayout) findViewById(R.id.event_View);

        eventDetail = new ArrayList<>();
        eventArray = new JSONArray();
        tempArray = new ArrayList<>();

        //initialize spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);


        //Initializing TextViews
        textViewEventTitle = (TextView) findViewById(R.id.textViewEventTitle);
        textViewEventDesc = (TextView) findViewById(R.id.textViewEventDesc);
        textViewEventTime = (TextView) findViewById(R.id.textViewEventTime);




        //checkLogged In
        //Fetching StaffID from shared preferences
        //If we will get true
        if (!loggedIn) {
            //We will start the Beacon_Main Activity
            Intent intent = new Intent(Beacon_MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        staffID = sharedPreferences.getString(Config.STAFF_ID, "Not Available");

        //Showing the current logged in email to textview
        textViewWelcome.setText("Welcome User " + staffID);


        // Use this check to determine whether BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            Toast.makeText(this, "Unable to obtain a BluetoothAdapter", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }


        //display list beacon
        if (savedInstanceState == null) {
            initList();
        }

           //get event from server
        getEventRespondTest(Volley.newRequestQueue(Beacon_MainActivity.this));
        spinner.setAdapter(new ArrayAdapter<>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));




        // set region parameters (UUID and unique region identifier)
        bstacInstance = Beaconstac.getInstance(this);
        bstacInstance.setRegionParams("F94DBB23-2266-7822-3782-57BEAC0952AC",
                "com.mobstac.beaconstac");
        bstacInstance.syncRules();

//        try {
//            bstacInstance.startRangingBeacons();
//        } catch (MSException e) {
//            // handle for older devices
//            TextView rangedView = (TextView) findViewById(R.id.RangedView);
//            rangedView.setText(R.string.ble_not_supported);
//            bCount.setVisibility(View.GONE);
//            testCamped.setVisibility(View.GONE);
//            e.printStackTrace();
//        }

        // if location is enabled
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            bstacInstance.syncPlaces();

            new PlaceSyncReceiver() {

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

        } else {
            // if location disabled, start ranging beacons
            try {
                bstacInstance.startRangingBeacons();
            } catch (MSException e) {
                // handle for older devices
                TextView rangedView = (TextView) findViewById(R.id.RangedView);
                rangedView.setText(R.string.ble_not_supported);
                bCount.setVisibility(View.GONE);
                testCamped.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }


        // display Check In event when beacon in range
        checkIn = (Button) findViewById(R.id.Check_in);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Beacon_MainActivity.this, ActivityCheckIn.class);
                intent.putExtra(Config.STAFF_ID, staffID);
                intent.putExtra(Config.EVENT_ID, eventCheckIN);
                startActivity(intent);

            }
        });

    }

    // end OnCreate
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onPause() {
        super.onPause();
        beaconAdapter.clear();
        beaconAdapter.notifyDataSetChanged();

        eventView.setVisibility(View.INVISIBLE);
        checkIn.setVisibility(View.INVISIBLE);

        tempRangedBeacon =0;
        bCount.setText("" + beacons.size());
        unregisterBroadcast();
        isPopupVisible = true;


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        tempRangedBeacon = 0;

        registerBroadcast();

        initList();
        bCount.setText("" + beacons.size());
        isPopupVisible = false;
//        getEventRespond();

//        // Call getEvent and display
//        getEvent();


    }

    @Override
    protected void onDestroy() {
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

    }

    // Callback intent results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
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
        Spinner beaconList = (Spinner) findViewById(R.id.beaconSpinner);
        beaconAdapter = new BeaconAdapter(beacons, this);
        beaconList.setAdapter(beaconAdapter);
        beaconList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                beaconUUID= beacons.get(position).getBeaconUUID().toString();
                beaconMajor = beacons.get(position).getMajor();
                beaconMinor = beacons.get(position).getMinor();

                Toast.makeText(getApplicationContext(),"Major: " +beaconMajor,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Minor: " +beaconMinor,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bCount = (TextView) findViewById(R.id.beaconCount);
        testCamped = (TextView) findViewById(R.id.CampedView);

//        eventAdapter = new ArrayAdapter<>(Beacon_MainActivity.this,android.R.layout.simple_spinner_dropdown_item,eventDetail);
//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setAdapter(eventAdapter);

        registerBroadcast();

//        getEventRespond();
//        spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));
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
            registerReceiver(beaconstacReceiver, intentFilter);

            //register place sync receiver
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_SUCCESS);
            iFilter.addAction(MSConstants.BEACONSTAC_INTENT_PLACE_SYNC_FAILURE);
            registerReceiver(placeSyncReceiver, iFilter);

            registered = true;
        }
    }

    private void unregisterBroadcast() {
        if (registered) {
            // unregister beaconstac receiver
            unregisterReceiver(beaconstacReceiver);
            // unregister place sync receiver
            unregisterReceiver(placeSyncReceiver);
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
            eventView.setVisibility(View.INVISIBLE);
            checkIn.setVisibility(View.INVISIBLE);
        }

        @Override
        public void rangedBeacons(Context context, ArrayList<MSBeacon> rangedBeacons) {
            beaconAdapter.clear();
            bCount.setText("" + rangedBeacons.size());
            beacons.addAll(rangedBeacons);
            beaconAdapter.notifyDataSetChanged();


            getEventRespondTest(Volley.newRequestQueue(Beacon_MainActivity.this));
//            spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));
//            Toast.makeText(Beacon_MainActivity.this,eventDetail.toString(),Toast.LENGTH_LONG).show();


//            if (rangedBeacons.size() != tempRangedBeacon) {
//                tempRangedBeacon = rangedBeacons.size();
//                //Setting adapter to show the items in the spinner
//                spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));
//            }

            if (tempArray.equals(eventDetail) && tempBeacons.equals(beacons)){
            }else {
                Toast.makeText(Beacon_MainActivity.this, "Beacons or events have been changed, fetching new data",Toast.LENGTH_SHORT).show();
                //Setting adapter to show the items in the spinner
                spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));
                tempArray=eventDetail;
                tempBeacons=beacons;

            }



            if (rangedBeacons.size()!=0) {
             checkIn.setVisibility(View.VISIBLE);
                eventView.setVisibility(View.VISIBLE);
            }else {
                checkIn.setVisibility(View.INVISIBLE);
                eventView.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void campedOnBeacon(Context context, MSBeacon beacon) {
            testCamped.setText("Camped: " + beacon.getMajor() + ":" + beacon.getMinor());
            beaconAdapter.addBeacon(beacon);
            beaconAdapter.notifyDataSetChanged();
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
                Toast.makeText(getApplicationContext(), "Rule " + ruleName, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void enteredRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getApplicationContext(), "Entered region", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedRegion(Context context, String region) {
            beaconAdapter.clear();
            beaconAdapter.notifyDataSetChanged();
            bCount.setText("" + beacons.size());
            Toast.makeText(getApplicationContext(), "Exited region", Toast.LENGTH_SHORT).show();
            eventView.setVisibility(View.INVISIBLE);
            checkIn.setVisibility(View.INVISIBLE);
        }

        @Override
        public void enteredGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Entered Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Exited Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
            checkIn.setVisibility(View.INVISIBLE);
        }
    };


////    private void getEventRespond() {
////        //Creating a string request
////        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        JSONObject j;
////                        try {
////                            //Parsing the fetched Json String to JSON Object
////                            j = new JSONObject(response);
////
////                            //Storing the Array of JSON String to our JSON Array
////                            eventArray = j.getJSONArray(Config.JSON_ARRAY);
////
////                            //Calling method getEventDetail to get the eventDetail from the JSON Array
////                            getEventDetail(eventArray);
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                },
////                new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////
////                    }
////
////                });
////
////        //Creating a request queue
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////
////        //Adding request to the queue
////        requestQueue.add(stringRequest);
////    }
////
////    private void getEventDetail(JSONArray j) {
////        //Traversing through all the items in the json array
////        for (int i = 0; i < j.length(); i++) {
////            try {
////                //Getting json object
////                JSONObject json = j.getJSONObject(i);
////
////                //Adding the name of the event to array list
////                    eventDetail.add(json.getString(Config.EVENT_TITLE));
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
////        spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventDetail));
////    }
//
    //Method to get eventID of a particular position
    private String getEventID(int position) {
        String EventID = "";
        try {
            //Getting object of given index
            JSONObject json = eventArray.getJSONObject(position);

            //Fetching EventID from that object
            EventID = json.getString(Config.EVENT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the EventTitle
        return EventID;
    }

    //Method to get event title of a particular position
    private String getTitle(int position) {
        String EventTitle = "";
        try {
            //Getting object of given index
            JSONObject json = eventArray.getJSONObject(position);

            //Fetching EventTitle from that object
            EventTitle = json.getString(Config.EVENT_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the EventTitle
        return EventTitle;
    }

    //Method to get event Desc of a particular position
    private String getDesc(int position) {
        String eventDescription = "";
        try {
            JSONObject json = eventArray.getJSONObject(position);
            eventDescription = json.getString(Config.EVENT_DESC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventDescription;
    }

    //Method to get event Time of a particular position
    private String getEventTime(int position) {
        String eventTime = "";
        try {
            JSONObject json = eventArray.getJSONObject(position);
            eventTime = json.getString(Config.EVENT_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventTime;
    }


    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textViews for a selected item
        textViewEventTitle.setText(getTitle(position));
        textViewEventDesc.setText(getDesc(position));
        textViewEventTime.setText(getEventTime(position));
        eventCheckIN = getEventID(position);
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        textViewEventTitle.setText("");
//        textViewEventDesc.setText("");
//        textViewEventTime.setText("");
    }





    //testing
    private void getEventRespondTest (RequestQueue requestQueue) {
        //Creating a string request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                            try {
                                eventArray = respond.getJSONArray("result");
                                eventDetail = getEventDetail(eventArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Beacon_MainActivity.this, "Unable to fetch data: " +error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                );
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private ArrayList getEventDetail(JSONArray j) {
        ArrayList event = new ArrayList();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the event to array list
                event.add(json.getString(Config.EVENT_TITLE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return event;
    }


    @Override
    public void onBackPressed() {
        exit();
    }
}
