package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import victory1908.nlbstafflogin2.R;

/**
 * Created by Victory1908 on 28-Dec-15.
 */

public class EventAdapterReAssignTest extends RecyclerView.Adapter<EventAdapterReAssignTest.ViewHolder> {

    private Context context;
    List<Event> events;
    Event eventSelected;
    private SparseBooleanArray mSparseBooleanArray;
    public int selectedPosition;

    public EventAdapterReAssignTest(Context context, List<Event> events, Event eventSelected) {
        this.context = context;
        this.events = events;
        this.eventSelected = eventSelected;
        mSparseBooleanArray = new SparseBooleanArray();
    }


    @Override
    public EventAdapterReAssignTest.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list_assign, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapterReAssignTest.ViewHolder holder, int position) {
        //Getting the particular item from the list
        Event event = events.get(position);

        //Showing data on the views
        holder.EventTitle.setHint(event.getEventTitle());
        holder.EventDesc.setText(event.getEventDesc());
        holder.EventStartTime.setText(event.getEventStartTime());
        holder.EventEndTime.setText(event.getEventEndTime());
        holder.EventID.setText(event.getEventID());

        //in some case, it will prevent unwanted situations;
        holder.selectCheckBox.setOnCheckedChangeListener(null);

        //if true, your check box will be selected, else unselected

        holder.selectCheckBox.setTag(position);

        if (position == selectedPosition) {
            holder.selectCheckBox.setChecked(true);
        } else {
            holder.selectCheckBox.setChecked(false);
        }
        holder.selectCheckBox.setOnCheckedChangeListener(new CheckListener(holder.selectCheckBox, position));
        eventSelected = getCheckedItems();
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return events.size();
    }

    class CheckListener implements CompoundButton.OnCheckedChangeListener {

        private CheckBox checkbox;
        Event data;
        int position;

        public CheckListener(CheckBox checkbox,int position) {

            this.checkbox = checkbox;
            this.position=position;

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            if (isChecked) {
                checkbox.setChecked(true);
                selectedPosition = position;
                EventAdapterReAssignTest.this.notifyDataSetChanged();
            } else {
                checkbox.setChecked(false);

            }
            buttonView.setChecked(isChecked);

        }

    }

    public Event getCheckedItems() {

        return events.get(selectedPosition);
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

            EventTitle = (EditText) itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView) itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView) itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView) itemView.findViewById(R.id.EventEndTime);
            EventID = (TextView) itemView.findViewById(R.id.EventID);

            selectCheckBox = (CheckBox) itemView.findViewById(R.id.selectCheckBox);
        }
    }
}


//public class EventAdapterReAssignTest extends RecyclerView.Adapter<EventAdapterReAssignTest.ViewHolder>{
//    private Context context;
//    List<Event> events, eventSelected;
//    private SparseBooleanArray mSparseBooleanArray;
//
//    public EventAdapterReAssignTest(Context context, List<Event> events, List<Event> eventSelected) {
//        this.context = context;
//        this.events = events;
//        this.eventSelected = eventSelected;
//        mSparseBooleanArray = new SparseBooleanArray();
//    }
//
//
//    @Override
//    public EventAdapterReAssignTest.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        // Inflate the custom layout
//        View eventView1 = inflater.inflate(R.layout.event_list_re_assign_test, parent, false);
//
//        // Return a new holder instance
//        ViewHolder viewHolder = new ViewHolder(eventView1);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(EventAdapterReAssignTest.ViewHolder holder, int position) {
//        //Getting the particular item from the list
//        Event event = events.get(position);
//
//        //Showing data on the views
//        holder.EventTitle.setHint(event.getEventTitle());
//        holder.EventDesc.setText(event.getEventDesc());
//        holder.EventStartTime.setText(event.getEventStartTime());
//        holder.EventEndTime.setText(event.getEventEndTime());
//        holder.EventID.setText(event.getEventID());
//
//        //in some case, it will prevent unwanted situations;
//        holder.selectCheckBox.setOnCheckedChangeListener(null);
//
//        //if true, your check box will be selected, else unselected
//        holder.selectCheckBox.setChecked(event.isSelected());
//        holder.selectCheckBox.setTag(position);
//
//        if(event.isSelected())
//        {
//            holder.selectCheckBox.setChecked(true);
//        }
//        else
//        {
//            holder.selectCheckBox.setChecked(false);
//        }
//        holder.selectCheckBox.setOnCheckedChangeListener(new CheckListener(event,holder.selectCheckBox));
//
//    }
//
//    // Return the total count of items
//    @Override
//    public int getItemCount() {
//        return events.size();
//    }
//
//    class CheckListener implements CompoundButton.OnCheckedChangeListener {
//
//        private CheckBox checkbox;
//        private Event data;
//
//        public CheckListener(Event data, CheckBox checkbox) {
//
//            this.checkbox = checkbox;
//            this.data = data;
//
//        }
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//
//            if (isChecked) {
//                checkbox.setChecked(true);
//
//            } else {
//                checkbox.setChecked(false);
//
//            }
//            buttonView.setChecked(isChecked);
//            data.setIsSelected(isChecked);
//        }
//
//    }
//
//    public ArrayList<Event> getCheckedItems() {
//        ArrayList<Event> mTempArry = new ArrayList<Event>();
//
//        for (int i = 0; i < events.size(); i++) {
//            if (mSparseBooleanArray.get(i)) {
//
//                Event data = events.get(i);
//
//                mTempArry.add(data);
//                eventSelected = mTempArry;
//            }
//        }
//
//        return mTempArry;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        //View
//        public EditText EventTitle;
//        public TextView EventDesc;
//        public TextView EventStartTime;
//        public TextView EventEndTime;
//        public TextView EventID;
//        public CheckBox selectCheckBox;
//
//        //initiating View
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            // display Check In event when beacon in range
//
//            EventTitle = (EditText)itemView.findViewById(R.id.EventTitle);
//            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
//            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
//            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
//            EventID = (TextView)itemView.findViewById(R.id.EventID);
//
//            selectCheckBox = (CheckBox)itemView.findViewById(R.id.selectCheckBox);
//
//        }
//    }
//}
