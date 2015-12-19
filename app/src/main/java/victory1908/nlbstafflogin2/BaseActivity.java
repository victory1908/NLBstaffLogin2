package victory1908.nlbstafflogin2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobstac.beaconstac.models.MSBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected boolean loggedIn;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLBstaffAttedance");
        toolbar.setLogo(R.drawable.nlblogo);

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

        //Creating a shared preference
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Creating editor to store values to shared preferences
        editor = sharedPreferences.edit();

        //checkLoggedIn
        loggedIn= sharedPreferences.getBoolean(Config.LOGGED_IN_SHARED_PREF, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        //Getting out sharedPreferences
//                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                        //Getting editor
//                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGED_IN_SHARED_PREF, false);

                        //Putting blank value to password
                        editor.putString(Config.PASSWORD, "");

                        //Saving the SharedPreferences
                        editor.apply();

                        //Starting login activity
////                        Intent intent = new Intent(Beacon_MainActivity.this, LoginActivity.class);
//                        startActivity(intent);

                        Intent goToLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        goToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
                        startActivity(goToLoginActivity);
                        finish();

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

    //exit function
    protected void exit() {
        //Creating an alert dialog to confirm exit
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to exit?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        boolean shouldExit = true;
                        Intent result = new Intent();
                        result.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        result.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        result.putExtra(MainActivity.EXTRA_EXIT, shouldExit);
                        setResult(Activity.RESULT_OK, result);
                        finish();

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

//    public static ArrayList eventDetail;
//
//    private ArrayList getEventRespondTest(MSBeacon beacon) {
//        //Creating a string request
//        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject j;
//                        JSONArray eventArray;
//                        try {
//                            //Parsing the fetched Json String to JSON Object
//                            j = new JSONObject(response);
//
//                            //Storing the Array of JSON String to our JSON Array
//                            eventArray = j.getJSONArray(Config.JSON_ARRAY);
//
//                            //Calling method getEventDetail to get the eventDetail from the JSON Array
//                            eventDetail.clear();
//                            eventDetail = getEventDetail(eventArray);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//
//                });
//
//        //Creating a request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//        return eventDetail;
//    }
//
//    private ArrayList getEventDetail(JSONArray j) {
//        //Traversing through all the items in the json array
//        ArrayList eventDetail = new ArrayList();
//        for (int i = 0; i < j.length(); i++) {
//            try {
//                //Getting json object
//                JSONObject json = j.getJSONObject(i);
//
//                //Adding the name of the event to array list
//                eventDetail.add(json.getString(Config.EVENT_TITLE));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return eventDetail;
//    }
//
//    //Method to get EventID of a particular position
//    private String getEventID(JSONArray eventArray, int position) {
//        String EventID = "";
//        try {
//            //Getting object of given index
//            JSONObject json = eventArray.getJSONObject(position);
//
//            //Fetching EventID from that object
//            EventID = json.getString(Config.EVENT_ID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //Returning the EventTitle
//        return EventID;
//    }

//    //Method to get event title of a particular position
//    private String getTitle(int position) {
//        String EventTitle = "";
//        try {
//            //Getting object of given index
//            JSONObject json = eventArray.getJSONObject(position);
//
//            //Fetching EventTitle from that object
//            EventTitle = json.getString(Config.EVENT_TITLE);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //Returning the EventTitle
//        return EventTitle;
//    }
//
//    //Method to get event Desc of a particular position
//    private String getDesc(int position) {
//        String eventDescription = "";
//        try {
//            JSONObject json = eventArray.getJSONObject(position);
//            eventDescription = json.getString(Config.EVENT_DESC);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return eventDescription;
//    }
//
//    //Method to get event Time of a particular position
//    private String getEventTime(int position) {
//        String eventTime = "";
//        try {
//            JSONObject json = eventArray.getJSONObject(position);
//            eventTime = json.getString(Config.EVENT_START_TIME);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return eventTime;
//    }


//    protected boolean isLoggedIn() {
//    //Fetching the boolean value form sharedPreferences
//        return sharedPreferences.getBoolean(Config.LOGGED_IN_SHARED_PREF,false);
//    }

}
