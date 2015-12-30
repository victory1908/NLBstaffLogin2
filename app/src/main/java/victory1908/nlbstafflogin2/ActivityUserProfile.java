package victory1908.nlbstafflogin2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;


public class ActivityUserProfile extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;


//    TextView eventid = (TextView) findViewById(R.id.Event_Today);

    private Button checkIn;



//    private String beaconUUID = "F94DBB23-2266-7822-3782-57BEAC0952AC";


    private JSONArray result;


    //Initializing the ArrayList
    private ArrayList<String> event = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textView = (TextView) findViewById(R.id.textViewStaffID);

        Intent intent = getIntent();

        textView.setText("Welcome User " + intent.getStringExtra(Config.STAFF_ID));

//        getEvent();

        checkIn = (Button) findViewById(R.id.CheckIn_Button);
        checkIn.setOnClickListener(this);
    }

//    // Display EventID from beaconUUID
//    private void getEvent() {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.EVENT_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        JSONObject j;
//                        try {
//                            //Parsing the fetched Json String to JSON Object
//                            j = new JSONObject(response);
//
//                            //Storing the Array of JSON String to our JSON Array
//                            result = j.getJSONArray(Config.JSON_ARRAY);
//
//                            //Calling method getEventID to get the EventID from the JSON Array
//                            getEventtID(result);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(ActivityUserProfile.this,error.toString(),Toast.LENGTH_LONG ).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<>();
//                map.put(Config.BEACON_UUID, BeaconAdapter.beaconUUID);
//                return map;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//
//    private void getEventtID(JSONArray j) {
//        //Traversing through all the items in the json array
//        for (int i = 0; i < j.length(); i++) {
//            try {
//                //Getting json object
//                JSONObject json = j.getJSONObject(i);
//
//                Toast.makeText(ActivityUserProfile.this,json.getString(Config.EVENT_ID),Toast.LENGTH_LONG ).show();
//                //Adding the EventID to array list
//                event.add(json.getString(Config.EVENT_ID));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ScanBeaconFragment.class);
        intent.putExtra(Config.STAFF_ID,LoginActivity.class);
        startActivity(intent);
    }
}
