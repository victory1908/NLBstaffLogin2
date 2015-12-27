package victory1908.nlbstafflogin2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.event.Event;

public class EditEventDetailActivity extends BaseActivity implements View.OnClickListener {

    Event event;
    Beacon beacon;
    EditText eventTitle;
    EditText eventDesc;
    EditText eventStartTime;
    EditText eventEndTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = getIntent().getParcelableExtra("event");
        eventTitle = (EditText) (findViewById(R.id.EventTitle));
        eventTitle.setText(event.getEventTitle());

        eventDesc = (EditText) (findViewById(R.id.EventDesc));
        eventDesc.setText(event.getEventDesc());

        eventStartTime = (EditText) (findViewById(R.id.EventStartTime));
        eventStartTime.setText(event.getEventStartTime());
        eventStartTime.setOnClickListener(this);

        eventEndTime = (EditText) (findViewById(R.id.EventEndTime));
        eventEndTime.setText(event.getEventEndTime());
        eventEndTime.setOnClickListener(this);

    }

    public void updateEvent (View view){

        event.setEventTitle(eventTitle.getText().toString());
        event.setEventDesc(eventDesc.getText().toString());
        event.setEventStartTime(eventStartTime.getText().toString());
        event.setEventEndTime(eventEndTime.getText().toString());

//        Toast.makeText(this, event.getEventTitle(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventDesc(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventStartTime(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventEndTime(),Toast.LENGTH_SHORT).show();

        //UpdateEvent
        updateConfirm();

    }

    public EditText setDateTime (final EditText selectDate) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);

        selectDate.setText("");
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                selectDate.append(hourOfDay + ":" +minute+":00");
            }
        },mHour,mMinute,true);
        timePickerDialog.show();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // dd.MM.yyyy
                selectDate.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth+" ");
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

        return selectDate;
    }

    @Override
    public void onClick(View v) {
        if (v==eventStartTime) eventStartTime = setDateTime(eventStartTime);
        if (v==eventEndTime) eventEndTime = setDateTime(eventEndTime);
    }

    private void updateEventData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditEventDetailActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditEventDetailActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.EVENT_ID, event.getEventID());
                map.put(Config.EVENT_TITLE, event.getEventTitle());
                map.put(Config.EVENT_DESC, event.getEventDesc());
                map.put(Config.EVENT_START_TIME, event.getEventStartTime());
                map.put(Config.EVENT_END_TIME, event.getEventEndTime());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // DeleteEvent
    public void deleteEvent (View view){

        event.setEventTitle(eventTitle.getText().toString());
        event.setEventDesc(eventDesc.getText().toString());
        event.setEventStartTime(eventStartTime.getText().toString());
        event.setEventEndTime(eventEndTime.getText().toString());

//        Toast.makeText(this, event.getEventTitle(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventDesc(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventStartTime(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventEndTime(),Toast.LENGTH_SHORT).show();

        //UpdateEvent
        deleteConfirm();

    }

    private void deleteEventData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.DELETE_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditEventDetailActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditEventDetailActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.EVENT_ID, event.getEventID());
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateConfirm() {
        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to update Event?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        updateEventData();
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
        alertDialogBuilder.setMessage("Are you sure you want to delete Event?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        deleteEventData();
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

}
