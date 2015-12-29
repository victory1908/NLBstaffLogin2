package victory1908.nlbstafflogin2.ManageEventFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Set;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.event.Event;

import victory1908.nlbstafflogin2.event.EventAdapterEdit;

public class EditEventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public EditEventFragment() {
        // Required empty public constructor
    }
    RequestQueue requestQueue;

    //JSON Array
    private JSONArray eventArray;

    //Creating a List of event
    private List<Event> listEvents,tempListEvents;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.activity_edit_event, container, false);

//        setContentView(R.layout.activity_edit_event);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);

        //Initializing Views
        recyclerView = (RecyclerView) viewFragment.findViewById(R.id.recycleView);

        swipeRefreshLayout = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        listEvents = new ArrayList();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventAdapterEdit(getContext(), listEvents);
        recyclerView.setAdapter(adapter);

        adapter = new EventAdapterEdit(getContext(), listEvents);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(getContext());

        getEventDetailRespond(requestQueue);
        tempListEvents = listEvents;

        //refresh when scroll till bottom
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView1, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //IfScrolled at last then
                    if (isLastItemDisplaying(recyclerView)) {
                        getEventDetailRespond(requestQueue);
                        if (!(listEvents.equals(tempListEvents))) {
                            adapter.notifyDataSetChanged();
                            tempListEvents = listEvents;
                        }
                    }
                }
            });
        } else {
            recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //IfScrolled at last then
                    if (isLastItemDisplaying(recyclerView)) {
                        getEventDetailRespond(requestQueue);
                        if (!(listEvents.equals(tempListEvents))) {
                            adapter.notifyDataSetChanged();
                            tempListEvents = listEvents;
                        }
                    }
                }
            });
        }

        return viewFragment;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
//            Toast.makeText(getContext(),"Edit event fragment",Toast.LENGTH_SHORT).show();
        }
    }


    private void getEventDetailRespond(RequestQueue requestQueue) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.GET_ALL_EVENT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            progressBar.setVisibility(View.GONE);

                            getEventDetail(eventArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Unable to fetch data event Detail: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    private void getEventDetail(JSONArray j) {
        listEvents.clear();
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
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerView scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        getEventDetailRespond(requestQueue);
        if (!(listEvents.equals(tempListEvents))) {
            adapter.notifyDataSetChanged();
            tempListEvents = listEvents;
        }
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
