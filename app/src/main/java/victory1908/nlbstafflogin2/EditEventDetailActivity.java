package victory1908.nlbstafflogin2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import victory1908.nlbstafflogin2.beaconstac.Beacon;
import victory1908.nlbstafflogin2.event.Event;

public class EditEventDetailActivity extends AppCompatActivity {

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

        event = getIntent().getParcelableExtra("event");
        eventTitle = (EditText) (findViewById(R.id.EventTitle));
        eventTitle.setHint(event.getEventTitle());

        eventDesc = (EditText) (findViewById(R.id.EventDesc));
        eventDesc.setHint(event.getEventDesc());

        eventStartTime = (EditText) (findViewById(R.id.EventStartTime));
        eventStartTime.setHint(event.getEventStartTime());

        eventEndTime = (EditText) (findViewById(R.id.EventEndTime));
        eventEndTime.setHint(event.getEventEndTime());

    }

    public void updateEvent (View view){
        Event event = new Event();
        event.setEventTitle(eventTitle.getText().toString());
        event.setEventDesc(eventDesc.getText().toString());
        event.setEventStartTime(eventStartTime.getText().toString());
        event.setEventEndTime(eventEndTime.getText().toString());

        Toast.makeText(this, event.getEventTitle(),Toast.LENGTH_LONG).show();
        Toast.makeText(this, event.getEventDesc(),Toast.LENGTH_LONG).show();

    }

}
