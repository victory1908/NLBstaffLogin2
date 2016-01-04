package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import victory1908.nlbstafflogin2.ActivityCheckIn;
import victory1908.nlbstafflogin2.R;

/**
 * Created by Victory1908 on 15-Dec-15.
 */
public class EventAdapter1 extends RecyclerView.Adapter<EventAdapter1.ViewHolder> {

    private Context context;
    List <Event> events;

    public EventAdapter1(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }


    @Override
    public EventAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter1.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Event event = events.get(position);

        //Showing data on the views
        holder.EventTitle.setText(event.getEventTitle());
        holder.EventDesc.setText(event.getEventDesc());
        holder.EventStartTime.setText(event.getEventStartTime());
        holder.EventEndTime.setText(event.getEventEndTime());
        holder.EventID = event.getEventID();

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //View
        public TextView EventTitle;
        public TextView EventDesc;
        public TextView EventStartTime;
        public TextView EventEndTime;
        private String EventID;
        public Button checkIn;


        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range
            checkIn = (Button) itemView.findViewById(R.id.CheckIn_Button);

            EventTitle = (TextView)itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
            EventTitle.setOnClickListener(this);
            checkIn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,ActivityCheckIn.class);
            intent.putExtra("event",events.get(getLayoutPosition()));
            context.startActivity(intent);

        }
    }
}
