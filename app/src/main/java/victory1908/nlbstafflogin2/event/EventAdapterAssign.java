package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        final Event event = events.get(position);

        //Showing data on the views
        holder.EventTitle.setHint(event.getEventTitle());
        holder.EventDesc.setText(event.getEventDesc());
        holder.EventStartTime.setText(event.getEventStartTime());
        holder.EventEndTime.setText(event.getEventEndTime());
        holder.EventID.setText(event.getEventID());

        //in some case, it will prevent unwanted situations;
        holder.selectCheckBox.setOnCheckedChangeListener(null);

        //if true, your check box will be selected, else unselected
        holder.selectCheckBox.setChecked(event.isSelected());

        holder.selectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                event.setIsSelected(isChecked);
                if (isChecked) {
                    eventSelected.add(event);
                } else {
                    eventSelected.remove(event);
                }
            }
        });

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

//            editBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked){
//                        eventSelected.add(events.get(getLayoutPosition()));
//                    }else
//                        eventSelected.remove(events.get(getLayoutPosition()));
//                }
//            });

        }
    }


}
