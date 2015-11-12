package victory1908.nlbstafflogin2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.BeaconAdapter;


public class ActivityUserProfile extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;


//    TextView eventid = (TextView) findViewById(R.id.Event_Today);

    private Button checkIn;



//    private String beaconUUID = "F94DBB23-2266-7822-3782-57BEAC0952AC";


    private JSONArray result;


    //Initializing the ArrayList
    private ArrayList<String> event = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textView = (TextView) findViewById(R.id.textViewStaffID);

        Intent intent = getIntent();

        textView.setText("Welcome User " + intent.getStringExtra(LoginActivity.KEY_STAFFID));

        getEvent();

        checkIn = (Button) findViewById(R.id.CheckIn_Button);
        checkIn.setOnClickListener(this);
    };

    // Display eventID from beaconUUID
    private void getEvent() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
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
                        Toast.makeText(ActivityUserProfile.this,error.toString(),Toast.LENGTH_LONG ).show();
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

                Toast.makeText(ActivityUserProfile.this,json.getString(Config.KEY_EVENT_ID),Toast.LENGTH_LONG ).show();
                //Adding the EventID to array list
                event.add(json.getString(Config.KEY_EVENT_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, victory1908.nlbstafflogin2.beaconstac.Beacon_MainActivity.class);
        intent.putExtra(LoginActivity.KEY_STAFFID,LoginActivity.class);
        startActivity(intent);
    }
}
