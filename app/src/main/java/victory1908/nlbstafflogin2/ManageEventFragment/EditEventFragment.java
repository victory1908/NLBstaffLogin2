package victory1908.nlbstafflogin2.ManageEventFragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import java.util.List;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.event.Event;
import victory1908.nlbstafflogin2.event.EventAdapterEdit;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;

public class EditEventFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public EditEventFragment() {
        // Required empty public constructor
    }
    RequestQueue requestQueue;

    //JSON Array
    private JSONArray eventArray;

    //Creating a List of event
    private List<Event> listEvents;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
//    private RecyclerView.Adapter adapter;
    EventAdapterEdit adapter;

    ProgressBar progressBar;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_edit_event, container, false);

//        setContentView(R.layout.fragment_edit_event);
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

//        adapter.setMyItemClickListener(new EventAdapterEdit.MyItemClickListener() {
//            @Override
//            public void onMyItemClick(int position, Event event) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("event", event);
//                Fragment fragment = new EditEventDetailFragment();
//                fragment.setArguments(bundle);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainDrawer,fragment).addToBackStack(null).commit();
//            }
//        });



        requestQueue = CustomVolleyRequest.getInstance(this.getContext().getApplicationContext()).getRequestQueue();

        getEventDetailRespond(requestQueue);

        //refresh when scroll till bottom
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //IfScrolled at last then
                    if (isLastItemDisplaying(recyclerView)) {
                        getEventDetailRespond(requestQueue);
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
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, Config.GET_ALL_EVENT_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            eventArray = new JSONArray();
                            eventArray = respond.getJSONArray("result");
                            progressBar.setVisibility(View.GONE);
                            listEvents.clear();
                            listEvents.addAll(getEventDetail(eventArray));
                            adapter.notifyDataSetChanged();
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


    @Override
    public void onRefresh() {
        getEventDetailRespond(requestQueue);
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
