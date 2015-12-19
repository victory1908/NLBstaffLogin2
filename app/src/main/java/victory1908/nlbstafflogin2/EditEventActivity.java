package victory1908.nlbstafflogin2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.event.Event;

import victory1908.nlbstafflogin2.event.EventAdapterEdit;

public class EditEventActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    //JSON Array
    private JSONArray eventArray;

    //Creating a List of event
    private List<Event> listEvents;
    private List<Beacon> beacons;
    Event eventChange;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        //Initializing our listEvents list
        listEvents = new ArrayList();
        eventChange = new Event();
        beacons = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventAdapterEdit(this,listEvents,beacons,eventChange);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        getEventDetailRespond(requestQueue);
    }

    private void getEventDetailRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.DATA_ALL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            getEventDetail(eventArray);
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditEventActivity.this, "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void getEventDetail(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                Event event = new Event();
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
}
