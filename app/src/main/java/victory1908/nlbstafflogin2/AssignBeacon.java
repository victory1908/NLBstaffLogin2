package victory1908.nlbstafflogin2;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import victory1908.nlbstafflogin2.ManageEventBeaconFragment.AssignEventToBeacon;
import victory1908.nlbstafflogin2.ManageEventBeaconFragment.ManageEventAssigned;

public class AssignBeacon extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

//    RequestQueue requestQueue;
//
//    //JSON Array
//    private JSONArray eventArray,beaconArray;
//
//    //Creating a List of event
//    private List<Event> listEvents, eventSelected;
//    private List<Beacon> listBeacons, beaconSelected;
//
//
//    //Creating Views
//    private RecyclerView recyclerView, beaconRecyclerView;
//    private RecyclerView.LayoutManager layoutManager, layoutMangerBeacon;
//    private RecyclerView.Adapter adapter, beaconAdapter;
//
//
//    ProgressBar progressBar;
//    Button assignBeaconToEvent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }


    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"Assign Event", "Manage Event"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AssignEventToBeacon();
                case 1:
                    return new ManageEventAssigned();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

    }




//        //Initializing our listEvents list
//        listEvents = new ArrayList();
//        listBeacons = new ArrayList<>();
//
//        listEvents.clear();
//        listBeacons.clear();
//
//        eventSelected = new ArrayList<>();
//        beaconSelected = new ArrayList<>();
//
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        assignBeaconToEvent = (Button) findViewById(R.id.assignEventBeacon);
//
//        //Initializing Views
//        recyclerView = (RecyclerView) findViewById(R.id.eventList);
//        beaconRecyclerView = (RecyclerView) findViewById(R.id.beaconList);
//
//        progressBar = (ProgressBar)findViewById(R.id.progressBar);
//
//        recyclerView.setHasFixedSize(true);
//        beaconRecyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        layoutMangerBeacon = new LinearLayoutManager(this);
//
//        recyclerView.setLayoutManager(layoutManager);
//        beaconRecyclerView.setLayoutManager(layoutMangerBeacon);
//
//        adapter = new EventAdapterAssign(this,listEvents,eventSelected);
//        beaconAdapter = new BeaconAdapterAssign(this,listBeacons,beaconSelected);
//
//        recyclerView.setAdapter(adapter);
//        beaconRecyclerView.setAdapter(beaconAdapter);
//
//        requestQueue = Volley.newRequestQueue(this);
//        getEventDetailRespond(requestQueue);
//        getBeaconRespond(requestQueue);

    }

//    @Override
//    protected void onResume() {
//        listEvents.clear();
//        listBeacons.clear();
//
//        getEventDetailRespond(requestQueue);
//        getBeaconRespond(requestQueue);
//
//        adapter.notifyDataSetChanged();
//        beaconAdapter.notifyDataSetChanged();
//        super.onResume();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        listEvents.clear();
//        listBeacons.clear();
//        eventSelected.clear();
//        beaconSelected.clear();
//
//        getEventDetailRespond(requestQueue);
//        adapter.notifyDataSetChanged();
//
//        getBeaconRespond(requestQueue);
//        beaconAdapter.notifyDataSetChanged();
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        listEvents.clear();
//        listBeacons.clear();
//        eventSelected.clear();
//        beaconSelected.clear();
//
//        getEventDetailRespond(requestQueue);
//        adapter.notifyDataSetChanged();
//
//        getBeaconRespond(requestQueue);
//        beaconAdapter.notifyDataSetChanged();
//    }
//
//    private void getEventDetailRespond(RequestQueue requestQueue) {
//
//        progressBar.setVisibility(View.VISIBLE);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.GET_ALL_EVENT_URL,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject respond) {
//                        try {
//                            eventArray = new JSONArray();
//                            eventArray = respond.getJSONArray("result");
//                            getEventDetail(eventArray);
//                            progressBar.setVisibility(View.GONE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AssignBeacon.this, "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//        //Adding request to the queue
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    private void getEventDetail(JSONArray j) {
//        listEvents.clear();
//        //Traversing through all the items in the json array
//        for (int i = 0; i < j.length(); i++) {
//            try {
//                //Getting json object
//                Event event = new Event();
//                JSONObject json = j.getJSONObject(i);
//
//                event.setEventID(json.getString(Config.EVENT_ID));
//                event.setEventTitle(json.getString(Config.EVENT_TITLE));
//                event.setEventDesc(json.getString(Config.EVENT_DESC));
//                event.setEventStartTime(json.getString(Config.EVENT_START_TIME));
//                event.setEventEndTime(json.getString(Config.EVENT_END_TIME));
//
//                //Adding the event object to the list
//                listEvents.add(event);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        //Notifying the adapter that data has been added or changed
//        adapter.notifyDataSetChanged();
//    }
//
//    private void getBeaconRespond(RequestQueue requestQueue) {
//
//        progressBar.setVisibility(View.VISIBLE);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Config.GET_ALL_BEACON_URL,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject respond) {
//                        try {
//                            beaconArray = new JSONArray();
//                            beaconArray = respond.getJSONArray("result");
//                            getBeaconDetail(beaconArray);
//                            progressBar.setVisibility(View.GONE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AssignBeacon.this, "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//        //Adding request to the queue
//        requestQueue.add(jsonObjectRequest);
//    }
//
//    private void getBeaconDetail(JSONArray j) {
//        listBeacons.clear();
//        //Traversing through all the items in the json array
//        for (int i = 0; i < j.length(); i++) {
//            try {
//                //Getting json object
//                Beacon beacon = new Beacon();
//                JSONObject json = j.getJSONObject(i);
//
//                beacon.setBeaconUUID(json.getString(Config.BEACON_UUID));
//                beacon.setBeaconMajor(json.getString(Config.BEACON_MAJOR));
//                beacon.setBeaconMinor(json.getString(Config.BEACON_MINOR));
//
//
//                //Adding the event object to the list
//                listBeacons.add(beacon);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        //Notifying the adapter that data has been added or changed
//        beaconAdapter.notifyDataSetChanged();
//    }
//
//
//
//
//    public void eventSelected (View view){
//        for (int i = 0; i <eventSelected.size() ; i++) {
//            Toast.makeText(this,eventSelected.get(i).getEventTitle().toString(),Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void beaconSelected (View view){
//        for (int i = 0; i <beaconSelected.size() ; i++) {
//            Toast.makeText(this,beaconSelected.get(i).getBeaconMajor().toString(),Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void assignConfirm(View view) {
//        //Creating an alert dialog to confirm update
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are you sure you want to assign Events to Beacons?");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
////                        for (int i = 0; i <beaconSelected.size() ; i++) {
////                            Toast.makeText(AssignBeacon.this,beaconSelected.get(i).beaconMajor, Toast.LENGTH_SHORT).show();
////                        }
//
//                        for (int i = 0; i < eventSelected.size() ; i++) {
//                            for (int j = 0; j < beaconSelected.size() ; j++) {
//                                assignEventToBeacon(eventSelected.get(i), beaconSelected.get(j));
//                            }
//                        }
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });
//
//        //Showing the alert dialog
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//
//    private void assignEventToBeacon(final Event event, final Beacon beacon) {
//
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setIndeterminate(true);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ASSIGN_BEACON_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(AssignBeacon.this, response, Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(AssignBeacon.this,error.toString(),Toast.LENGTH_SHORT ).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> map = new HashMap<>();
//                map.put(Config.EVENT_ID, event.getEventID());
//                map.put(Config.BEACON_UUID,beacon.getBeaconUUID());
//                map.put(Config.BEACON_MAJOR,beacon.getBeaconMajor());
//                map.put(Config.BEACON_MINOR,beacon.getBeaconMinor());
//                return map;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
//}
