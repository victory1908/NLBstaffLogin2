package victory1908.nlbstafflogin2.ManageEventFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.event.Event;

public class CreateEventFragment extends Fragment implements View.OnClickListener{

    public CreateEventFragment(){
        //Constructor
    }

    Event event;
    EditText eventTitle;
    EditText eventDesc;
    EditText eventStartTime;
    EditText eventEndTime;
    EditText eventID;

    Button createEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_create_event, container, false);

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
        eventTitle = (EditText)viewFragment.findViewById(R.id.EventTitle);
        eventDesc = (EditText)viewFragment.findViewById(R.id.EventDesc);
        eventStartTime = (EditText)viewFragment.findViewById(R.id.EventStartTime);
        eventEndTime = (EditText)viewFragment.findViewById(R.id.EventEndTime);
        eventID = (EditText)viewFragment.findViewById(R.id.EventID);
        createEvent = (Button)viewFragment.findViewById(R.id.createEvent);

        event = new Event();
        eventStartTime.setOnClickListener(this);
        eventEndTime.setOnClickListener(this);
        createEvent.setOnClickListener(this);

        return viewFragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
//            Toast.makeText(getContext(),"Create event fragment",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void createEvent (){

        event.setEventTitle(eventTitle.getText().toString());
        event.setEventDesc(eventDesc.getText().toString());
        event.setEventStartTime(eventStartTime.getText().toString());
        event.setEventEndTime(eventEndTime.getText().toString());
        event.setEventID(eventID.getText().toString());

        //UpdateEvent
        createConfirm();

    }


    private void createConfirm() {
        //Creating an alert dialog to confirm update
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG ).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                selectDate.append(hourOfDay + ":" +minute+":00");
            }
        },mHour,mMinute,true);
        timePickerDialog.show();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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
        if (v==createEvent) createEvent();
    }

}

