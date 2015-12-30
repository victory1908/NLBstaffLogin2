package victory1908.nlbstafflogin2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;

public class BaseFragment extends Fragment {

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected boolean loggedIn;

    ProgressBar progressBar;

    private static final String TAG = BaseFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.beacon_activity_main, container, false);

        return viewFragment;
    }


        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.beacon_activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("NLBstaffAttedance");
//        toolbar.setLogo(R.drawable.nlblogo);


        //Creating a shared preference
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Creating editor to store values to shared preferences
        editor = sharedPreferences.edit();

        //checkLoggedIn
        loggedIn= sharedPreferences.getBoolean(Config.LOGGED_IN_SHARED_PREF, false);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
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
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
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
////                        Intent intent = new Intent(ScanBeaconFragment.this, LoginActivity.class);
//                        startActivity(intent);

                        Intent goToLoginActivity = new Intent(getContext(), LoginActivity.class);
                        goToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
                        startActivity(goToLoginActivity);
                        getActivity().finish();

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
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
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
                        getActivity().setResult(Activity.RESULT_OK, result);
                        getActivity().finish();

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


    protected List<Beacon> getBeaconDetail(JSONArray j) {
        List<Beacon> beacons = new ArrayList<>();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            Beacon beacon = new Beacon();
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                beacon.setBeaconName(json.getString(Config.BEACON_NAME));
                beacon.setBeaconID(json.getString(Config.BEACON_ID));
                beacon.setBeaconSN(json.getString(Config.BEACON_SN));
                beacon.setBeaconUUID(json.getString(Config.BEACON_UUID));
                beacon.setMajor(Integer.valueOf(json.getString(Config.BEACON_MAJOR)));
                beacon.setMinor(Integer.valueOf(json.getString(Config.BEACON_MINOR)));

                beacons.add(beacon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beacons;
    }

    protected List<Event> getEventDetail(JSONArray j) {
        List<Event> events = new ArrayList<>();
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                Event event = new Event();
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                event.setEventID(json.getString(Config.EVENT_ID));
                event.setEventTitle(json.getString(Config.EVENT_TITLE));
                event.setEventDesc(json.getString(Config.EVENT_DESC));
                event.setEventStartTime(json.getString(Config.EVENT_START_TIME));
                event.setEventEndTime(json.getString(Config.EVENT_END_TIME));

                //Adding the event object to the list
                events.add(event);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return events;
    }

    //This method would check that the recyclerView scroll has reached the bottom or not
    protected boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }


    //Test fetching data template (Currently not work as need final variable)
    protected List<Event> getEventRespond(RequestQueue requestQueue, String url, Beacon beacon) {
        progressBar.setVisibility(View.VISIBLE);
        final List<Event> listEvents = new ArrayList<>();

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put(Config.BEACON_UUID, beacon.getBeaconUUID());
        jsonParams.put(Config.BEACON_MAJOR, beacon.getMajor());
        jsonParams.put(Config.BEACON_MINOR, beacon.getMinor());

        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            JSONArray eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            listEvents.clear();
                            listEvents.addAll(getEventDetail(eventArray));
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
        return listEvents;
    }




}
