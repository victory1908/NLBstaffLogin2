package victory1908.nlbstafflogin2;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterDetail;


public class ActivityCheckIn extends BaseActivity {

    //Creating a List of event
    private List<Event> listEvents;
    Event event;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_check_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLBstaffAttedance");
        toolbar.setLogo(R.drawable.nlblogo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our listBeacons list
        listEvents = new ArrayList();

        adapter = new EventAdapterDetail(this, listEvents);
        recyclerView.setAdapter(adapter);


        //perform checkIn
        checkIn();




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    private void checkIn() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CHECK_IN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ActivityCheckIn.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityCheckIn.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.STAFF_ID, sharedPreferences.getString(Config.STAFF_ID,"Not Available"));
//                map.put(Config.EVENT_ID,getIntent().getStringExtra(Config.EVENT_ID));
                event = getIntent().getParcelableExtra("event");
                listEvents.clear();
                listEvents.add(event);
                adapter.notifyDataSetChanged();
                map.put(Config.EVENT_ID, event.getEventID());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
