package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobstac.beaconstac.models.MSBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.beaconstac.Beacon;

/**
 * Created by Victory1908 on 08-Dec-15.
 */
public class Event implements Parcelable {
    private String EventID;
    private String EventDesc;
    private String EventTitle;
    private String EventStartTime;
    private String EventEndTime;

    private ArrayList<Beacon> beacons;
    private JSONArray beaconArray;
    private ProgressBar progressBar;
    private Context context;
    RequestQueue requestQueue;


    public ArrayList<Beacon> getBeacons() {
//        getBeaconRespond(requestQueue, EventID );
        return beacons;
    }

    public void setBeacons(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getEventDesc() {
        return EventDesc;
    }

    public void setEventDesc(String eventDesc) {
        EventDesc = eventDesc;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventStartTime() {
        return EventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        EventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return EventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        EventEndTime = eventEndTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.EventID);
        dest.writeString(this.EventDesc);
        dest.writeString(this.EventTitle);
        dest.writeString(this.EventStartTime);
        dest.writeString(this.EventEndTime);
    }

    public Event() {
    }

    protected Event(Parcel in) {
        this.EventID = in.readString();
        this.EventDesc = in.readString();
        this.EventTitle = in.readString();
        this.EventStartTime = in.readString();
        this.EventEndTime = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };


    private void getBeaconRespond(RequestQueue requestQueue, String EventID) {
        progressBar = new ProgressBar(context);
        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        final JSONObject params = new JSONObject();
        try {
            params.put(Config.EVENT_ID, EventID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Creating a JSONObject request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_BEACON_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            beaconArray = new JSONArray();
                            beacons = new ArrayList<>();

                            beaconArray = respond.getJSONArray("result");
                            Toast.makeText(context, beaconArray.toString(), Toast.LENGTH_LONG).show();
                            getBeaconFromEventID(beaconArray);

                            Toast.makeText(context, beacons.toString(),Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Unable to fetch data beacon Info: " +error.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                return headers;
            }
        };

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);

    }

    private void getBeaconFromEventID(JSONArray jsonArray) {
        //Traversing through all the items in the json array
        for (int i = 0; i < jsonArray.length(); i++) {
            Beacon beacon = new Beacon();
            JSONObject jsonObject = null;
            try {
                //Getting json object
                jsonObject = jsonArray.getJSONObject(i);

                //Adding the name of the event to array list
                beacon.setBeaconUUID(jsonObject.getString(Config.BEACON_UUID));
                beacon.setBeaconMajor(jsonObject.getString(Config.BEACON_MAJOR));
                beacon.setBeaconMinor(jsonObject.getString(Config.BEACON_MINOR));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            beacons.add(beacon);
        }
//        Toast.makeText(context, beacons.toString(),Toast.LENGTH_LONG).show();
    }

}