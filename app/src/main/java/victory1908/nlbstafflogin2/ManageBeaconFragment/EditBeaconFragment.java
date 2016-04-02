package victory1908.nlbstafflogin2.ManageBeaconFragment;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.annimon.stream.Stream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import victory1908.nlbstafflogin2.BaseFragment;
import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.beaconstac.BeaconAdapterDetail;
import victory1908.nlbstafflogin2.request.CustomJsonObjectRequest;
import victory1908.nlbstafflogin2.request.CustomVolleyRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditBeaconFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    RequestQueue requestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    //JSON Array
    private JSONArray beaconArray;
    private List<Beacon> listBeacons;
    //Creating Views
    private RecyclerView beaconRecyclerView;
    private RecyclerView.LayoutManager layoutMangerBeacon;
    private RecyclerView.Adapter beaconAdapter;


    public EditBeaconFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_edit_registered_beacon, container, false);


        requestQueue = CustomVolleyRequest.getInstance(this.getContext().getApplicationContext()).getRequestQueue();
        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);
        listBeacons = new ArrayList<>();

        Stream.of(listBeacons)
                .filter(value -> value.getBeaconName().contains("vic"));

        beaconRecyclerView = (RecyclerView)viewFragment.findViewById(R.id.beaconList);
        swipeRefreshLayout = (SwipeRefreshLayout)viewFragment.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        beaconRecyclerView.setHasFixedSize(true);
        layoutMangerBeacon = new LinearLayoutManager(getContext());
        beaconRecyclerView.setLayoutManager(layoutMangerBeacon);
        beaconAdapter = new BeaconAdapterDetail(getContext(),listBeacons);
        beaconRecyclerView.setAdapter(beaconAdapter);

        getBeaconRespond(requestQueue);

        //refresh when scroll till bottom
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            beaconRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //IfScrolled at last then
                    if (isLastItemDisplaying(beaconRecyclerView)) {
                        getBeaconRespond(requestQueue);
                    }
                }
            });
        } else {
            beaconRecyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //IfScrolled at last then
                    if (isLastItemDisplaying(beaconRecyclerView)) {
                        getBeaconRespond(requestQueue);
                    }
                }
            });
        }

        // Inflate the layout for this fragment
        return viewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getBeaconRespond(RequestQueue requestQueue) {

        progressBar.setVisibility(View.VISIBLE);
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, Config.GET_ALL_BEACON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respond) {
                        try {
                            progressBar.setVisibility(View.GONE);

                            beaconArray = new JSONArray();
                            beaconArray = respond.getJSONArray("result");

                            listBeacons.clear();
                            listBeacons.addAll(getBeaconDetail(beaconArray));
                            beaconAdapter.notifyDataSetChanged();

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
        getBeaconRespond(requestQueue);
        if (swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
    }
}
