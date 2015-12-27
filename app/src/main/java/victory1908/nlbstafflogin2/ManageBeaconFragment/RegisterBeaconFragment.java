package victory1908.nlbstafflogin2.ManageBeaconFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.HashMap;
import java.util.Map;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;

public class RegisterBeaconFragment extends Fragment implements View.OnClickListener {

    EditText beaconName;
    EditText beaconID;
    EditText beaconSN;
    EditText beaconUUID;
    EditText beaconMajor;
    EditText beaconMinor;
    Button registerBeacon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.activity_register_beacon, container, false);

//        setContentView(R.layout.activity_register_beacon);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton)viewFragment.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        beaconName = (EditText)viewFragment.findViewById(R.id.beaconName);
        beaconID = (EditText)viewFragment.findViewById(R.id.beaconID);
        beaconSN = (EditText)viewFragment.findViewById(R.id.beaconSN);
        beaconUUID = (EditText)viewFragment.findViewById(R.id.beaconUUID);
        beaconMajor = (EditText)viewFragment.findViewById(R.id.beaconMajor);
        beaconMinor = (EditText)viewFragment.findViewById(R.id.beaconMinor);
        registerBeacon = (Button)viewFragment.findViewById(R.id.registerBeacon);
        registerBeacon.setOnClickListener(this);

        return viewFragment;
    }


    public void registerBeacon() {

        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you sure you want to update Event?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        registerBeaconData();
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

    private void registerBeaconData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_BEACON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.BEACON_NAME, beaconName.getText().toString());
                map.put(Config.BEACON_ID, beaconID.getText().toString());
                map.put(Config.BEACON_SN, beaconSN.getText().toString());

                map.put(Config.BEACON_UUID, beaconUUID.getText().toString());
                map.put(Config.BEACON_MAJOR, beaconMajor.getText().toString());
                map.put(Config.BEACON_MINOR, beaconMinor.getText().toString());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v==registerBeacon)registerBeacon();
    }
}
