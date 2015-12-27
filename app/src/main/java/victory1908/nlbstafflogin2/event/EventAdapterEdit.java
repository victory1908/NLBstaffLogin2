package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import victory1908.nlbstafflogin2.Config;
import victory1908.nlbstafflogin2.EditEventDetailActivity;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;

/**
 * Created by Victory1908 on 15-Dec-15.
 */
public class EventAdapterEdit extends RecyclerView.Adapter<EventAdapterEdit.ViewHolder> {

    private Context context;
    List<Event> events;

    public EventAdapterEdit(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventAdapterEdit.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list_edit, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapterEdit.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Event event = events.get(position);
//        requestQueue = Volley.newRequestQueue(context);
//        getBeaconRespond(requestQueue, event);

        //Showing data on the views
        holder.EventTitle.setHint(event.getEventTitle());
        holder.EventDesc.setText(event.getEventDesc());
        holder.EventStartTime.setText(event.getEventStartTime());
        holder.EventEndTime.setText(event.getEventEndTime());
        holder.EventID.setText(event.getEventID());

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //View
        public EditText EventTitle;
        public TextView EventDesc;
        public TextView EventStartTime;
        public TextView EventEndTime;
        public TextView EventID;
        public Button Edit;


        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range
            Edit = (Button) itemView.findViewById(R.id.EditButton);

            EventTitle = (EditText)itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
            EventID = (TextView)itemView.findViewById(R.id.EventID);

//            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

            Edit.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditEventDetailActivity.class);
            intent.putExtra("event",events.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }


}
