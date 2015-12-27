package victory1908.nlbstafflogin2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.Beacon;

public class EditRegisterBeacon extends BaseActivity implements View.OnClickListener{

    Beacon beacon,beaconUpdate;
    EditText beaconName,beaconID,beaconSN,beaconUUID,beaconMajor,beaconMinor;
    Button editRegisterBeacon,deleteRegisterBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_register_beacon);
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

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beacon = getIntent().getParcelableExtra("beacon");
        beaconName = (EditText) findViewById(R.id.beaconName);
        beaconID = (EditText) findViewById(R.id.beaconID);
        beaconSN = (EditText) findViewById(R.id.beaconSN);
        beaconUUID = (EditText) findViewById(R.id.beaconUUID);
        beaconMajor = (EditText) findViewById(R.id.beaconMajor);
        beaconMinor = (EditText) findViewById(R.id.beaconMinor);

        editRegisterBeacon = (Button) findViewById(R.id.editRegisterBeacon);
        deleteRegisterBeacon = (Button) findViewById(R.id.deleteRegisterBeacon);

        editRegisterBeacon.setOnClickListener(this);
        deleteRegisterBeacon.setOnClickListener(this);

        beaconName.setText(beacon.getBeaconName());
        beaconID.setText(beacon.getBeaconID());
        beaconSN.setText(beacon.getBeaconSN());
        beaconUUID.setText(beacon.getBeaconUUID());
        beaconMajor.setText(String.valueOf(beacon.getMajor()));
        beaconMinor.setText(String.valueOf(beacon.getMinor()));

    }

    @Override
    public void onClick(View view) {

        beacon.setBeaconName(beaconName.getText().toString());
        beacon.setBeaconID(beaconID.getText().toString());
        beacon.setBeaconSN(beaconSN.getText().toString());
        beacon.setBeaconUUID(beaconUUID.getText().toString());
        beacon.setMajor(Integer.parseInt(beaconMajor.getText().toString()));
        beacon.setMinor(Integer.parseInt(beaconMinor.getText().toString()));

        if ((view == editRegisterBeacon)) updateConfirm();
        if ((view == deleteRegisterBeacon)) deleteConfirm();

    }

    private void updateConfirm() {
        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to update Beacon?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateBeaconData();
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

    private void deleteConfirm() {
        //Creating an alert dialog to confirm delete
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete Beacon?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        deleteBeaconData();
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


    private void updateBeaconData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_BEACON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put(Config.BEACON_NAME, beacon.getBeaconName());
                map.put(Config.BEACON_ID, beacon.getBeaconID());
                map.put(Config.BEACON_SN, beacon.getBeaconSN());
                map.put(Config.BEACON_UUID, beacon.getBeaconUUID());
                map.put(Config.BEACON_MAJOR, String.valueOf(beacon.getMajor()));
                map.put(Config.BEACON_MINOR, String.valueOf(beacon.getMinor()));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // DeleteBeacon
    private void deleteBeaconData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DELETE_BEACON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put(Config.BEACON_NAME, beacon.getBeaconName());
                map.put(Config.BEACON_ID, beacon.getBeaconID());
                map.put(Config.BEACON_SN, beacon.getBeaconSN());
                map.put(Config.BEACON_UUID, beacon.getBeaconUUID());
                map.put(Config.BEACON_MAJOR, String.valueOf(beacon.getMajor()));
                map.put(Config.BEACON_MINOR, String.valueOf(beacon.getMinor()));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
