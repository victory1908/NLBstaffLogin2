package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.EditEventDetailActivity;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;

/**
 * Created by Victory1908 on 15-Dec-15.
 */
public class EventAdapterEdit extends RecyclerView.Adapter<EventAdapterEdit.ViewHolder> {

    private Context context;
    List <Event> events;
    List<Beacon> beacons;
    Event eventChange;

    public EventAdapterEdit(Context context, List<Event> events, List<Beacon> beacons, Event eventChange) {
        this.context = context;
        this.events = events;
        this.beacons = beacons;
        this.eventChange = eventChange;
    }

    JSONArray beaconArray;
    ProgressBar progressBar;
    RequestQueue requestQueue;

    @Override
    public EventAdapterEdit.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list_edit, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapterEdit.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Event event = events.get(position);
//        requestQueue = Volley.newRequestQueue(context);
//        getBeaconRespond(requestQueue, event);

        //Showing data on the views
        holder.EventTitle.setHint(event.getEventTitle());
        holder.EventDesc.setText(event.getEventDesc());
        holder.EventStartTime.setText(event.getEventStartTime());
        holder.EventEndTime.setText(event.getEventEndTime());
        holder.EventID = event.getEventID();
        holder.BeaconAssigned.setText(event.getEventID());

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //View
        public EditText EventTitle;
        public TextView EventDesc;
        public TextView EventStartTime;
        public TextView EventEndTime;
        public TextView BeaconAssigned;
        private String EventID;
        public Button Edit;


        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range
            Edit = (Button) itemView.findViewById(R.id.EditButton);

            EventTitle = (EditText)itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
            BeaconAssigned = (TextView)itemView.findViewById(R.id.BeaconAssigned);

//            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

            Edit.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditEventDetailActivity.class);
            intent.putExtra("event",events.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }

    private void getBeaconRespond(RequestQueue requestQueue, Event event) {

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        final JSONObject params = new JSONObject();
        try {
            params.put(Config.EVENT_ID, event.getEventID());
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
                            Toast.makeText(context, beaconArray.toString(),Toast.LENGTH_LONG).show();
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
