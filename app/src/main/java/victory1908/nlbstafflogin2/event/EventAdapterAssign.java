package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import victory1908.nlbstafflogin2.R;
import victory1908.nlbstafflogin2.beaconstac.Beacon;

/**
 * Created by Victory1908 on 15-Dec-15.
 */
public class EventAdapterAssign extends RecyclerView.Adapter<EventAdapterAssign.ViewHolder> {

    private Context context;
    List <Event> events, eventSelected;

    public EventAdapterAssign(Context context, List<Event> events, List<Event> eventSelected) {
        this.context = context;
        this.events = events;
        this.eventSelected = eventSelected;
    }


    @Override
    public EventAdapterAssign.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list_assign, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapterAssign.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Event event = events.get(position);

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
        public CheckBox selectCheckBox;

        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range

            EventTitle = (EditText)itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
            EventID = (TextView)itemView.findViewById(R.id.EventID);

            selectCheckBox = (CheckBox)itemView.findViewById(R.id.selectCheckBox);
            selectCheckBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked){
                eventSelected.add(events.get(getLayoutPosition()));
            }else {
                if (eventSelected.contains(events.get(getLayoutPosition())))
                eventSelected.remove(events.get(getLayoutPosition()));
            }
        }
    }

}
