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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.Map;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.LoginActivity;
import victory1908.nlbstafflogin2.R;


public class Beacon_MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final ArrayList<String> event = new ArrayList<String>();
    private TextView textView;

    private static final String TAG = Beacon_MainActivity.class.getSimpleName();

    private ArrayList<MSBeacon> beacons = new ArrayList<MSBeacon>();

    private BeaconAdapter beaconAdapter;
    private TextView bCount;
    private TextView testCamped;
    private Button checkIn;
    Beaconstac bstacInstance;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    private boolean registered = false;
    private boolean isPopupVisible = false;


    //snipper
    //Declaring an Spinner
    private Spinner spinner;

    //An ArrayList for Spinner Items
    private ArrayList<String> eventdetail;

    //JSON Array
    private JSONArray result;

    //TextViews to display details
    private TextView textViewName;
    private TextView textViewCourse;
    private TextView textViewSession;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_activity_main);

        textView = (TextView) findViewById(R.id.textView_staffid);

        Intent intent = getIntent();

        textView.setText("Welcome User " + intent.getStringExtra(LoginActivity.KEY_STAFFID));


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

        if (savedInstanceState == null) {
            initList();
        }

        // set region parameters (UUID and unique region identifier)
        bstacInstance = Beaconstac.getInstance(this);
        bstacInstance.setRegionParams("F94DBB23-2266-7822-3782-57BEAC0952AC",
                "com.mobstac.beaconstac");
        bstacInstance.syncRules();

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

        //Create Spinner
        //Initializing the ArrayList
        eventdetail = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCourse = (TextView) findViewById(R.id.textViewCourse);
        textViewSession = (TextView) findViewById(R.id.textViewSession);
        //end spinner

    }

    // end oncreate

    private void initList() {
        ListView beaconList = (ListView) findViewById(R.id.beaconListView);
        beaconAdapter   = new BeaconAdapter(beacons, this);
        beaconList.setAdapter(beaconAdapter);

        bCount = (TextView) findViewById(R.id.beaconCount);
        testCamped = (TextView) findViewById(R.id.CampedView);
        registerBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconAdapter.clear();
        beaconAdapter.notifyDataSetChanged();
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
        initList();
        bCount.setText("" + beacons.size());
        registerBroadcast();
        isPopupVisible = false;

        // Call getEvent and display
        getEvent();

        //This method will fetch the data from the URL
        geteventdetail();
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
        }

        @Override
        public void rangedBeacons(Context context, ArrayList<MSBeacon> rangedBeacons) {
            beaconAdapter.clear();
            bCount.setText("" + rangedBeacons.size());
            beacons.addAll(rangedBeacons);
            beaconAdapter.notifyDataSetChanged();

            Toast.makeText(Beacon_MainActivity.this, "Scanning for beacon", Toast.LENGTH_SHORT).show();
            Button checkIn = (Button) findViewById(R.id.Check_in);
            checkIn.setVisibility(View.VISIBLE);
            checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Beacon_MainActivity.this, victory1908.nlbstafflogin2.ActivtityCheckIn.class);
                    intent.putExtra(LoginActivity.KEY_STAFFID,LoginActivity.class);
                    startActivity(intent);

                }
            });

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
        }

        @Override
        public void enteredGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Entered Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exitedGeofence(Context context, ArrayList<MSPlace> places) {
            Toast.makeText(getApplicationContext(), "Exited Geofence " + places.get(0).getName(), Toast.LENGTH_SHORT).show();
        }
    };


    // Display eventID from beaconUUID
    private void getEvent() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        JSONArray result = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getEventID to get the eventID from the JSON Array
                            getEventtID(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Beacon_MainActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_BEACON_UUID, BeaconAdapter.beaconUUID);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getEventtID(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                Toast.makeText(Beacon_MainActivity.this,json.getString(Config.KEY_EVENT_ID),Toast.LENGTH_LONG ).show();
                //Adding the EventID to array list
                event.add(json.getString(Config.KEY_EVENT_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void geteventdetail(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        JSONArray eventid = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            eventid = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method geteventdetail to get the eventdetail from the JSON Array
                            geteventdetail(eventid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(event.toString(), event.toString());
                return map;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void geteventdetail(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                eventdetail.add(json.getString(Config.KEY_EVENT_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(Beacon_MainActivity.this, android.R.layout.simple_spinner_dropdown_item, eventdetail));
    }

    //Method to get student name of a particular position
    private String getTitle(int position){
        String EventTitle="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching EventTitle from that object
            EventTitle = json.getString(Config.EVENT_TITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the EventTitle
        return EventTitle;
    }

    //Doing the same with this method as we did with getTitle()
    private String getDesc(int position){
        String course="";
        try {
            JSONObject json = result.getJSONObject(position);
            course = json.getString(Config.EVENT_DESC);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    //Doing the same with this method as we did with getTitle()
    private String getEventTime(int position){
        String session="";
        try {
            JSONObject json = result.getJSONObject(position);
            session = json.getString(Config.EVENT_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return session;
    }


    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        textViewName.setText(getTitle(position));
        textViewCourse.setText(getDesc(position));
        textViewSession.setText(getEventTime(position));
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewName.setText("");
        textViewCourse.setText("");
        textViewSession.setText("");
    }

}