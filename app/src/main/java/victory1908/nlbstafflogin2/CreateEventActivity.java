package victory1908.nlbstafflogin2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener{

    Event event;
    Beacon beacon;
    EditText eventTitle;
    EditText eventDesc;
    EditText eventStartTime;
    EditText eventEndTime;
    EditText eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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

        event = new Event();
        eventTitle = (EditText) (findViewById(R.id.EventTitle));
        eventDesc = (EditText) (findViewById(R.id.EventDesc));
        eventStartTime = (EditText) (findViewById(R.id.EventStartTime));
        eventEndTime = (EditText) (findViewById(R.id.EventEndTime));
        eventID = (EditText) (findViewById(R.id.EventID));

        eventStartTime.setOnClickListener(this);
        eventEndTime.setOnClickListener(this);

    }

    public void createEvent (View view){

        event.setEventTitle(eventTitle.getText().toString());
        event.setEventDesc(eventDesc.getText().toString());
        event.setEventStartTime(eventStartTime.getText().toString());
        event.setEventEndTime(eventEndTime.getText().toString());
        event.setEventID(eventID.getText().toString());

//        Toast.makeText(this, event.getEventTitle(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventDesc(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventStartTime(),Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, event.getEventEndTime(),Toast.LENGTH_SHORT).show();

        //UpdateEvent
        createConfirm();

    }


    private void createConfirm() {
        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to create Event?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        createEventData();
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

    private void createEventData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_EVENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CreateEventActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateEventActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
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


}

