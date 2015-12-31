package victory1908.nlbstafflogin2.event;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import victory1908.nlbstafflogin2.MainActivity;
import victory1908.nlbstafflogin2.ManageEventBeaconFragment.ManageEventAssignedBeaconFragment;
import victory1908.nlbstafflogin2.R;

/**
 * Created by Victory1908 on 15-Dec-15.
 */
public class EventAdapterReAssign extends RecyclerView.Adapter<EventAdapterReAssign.ViewHolder> {

    private Context context;
    List<Event> events;

    public EventAdapterReAssign(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public EventAdapterReAssign.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView1 = inflater.inflate(R.layout.event_list_re_assign, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapterReAssign.ViewHolder holder, int position) {
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
        public Button EditAssignBeacon;

        //initiating View
        public ViewHolder(View itemView) {
            super(itemView);

            // display Check In event when beacon in range
            EditAssignBeacon = (Button) itemView.findViewById(R.id.EditAssignBeacon);

            EventTitle = (EditText)itemView.findViewById(R.id.EventTitle);
            EventDesc = (TextView)itemView.findViewById(R.id.EventDesc);
            EventStartTime = (TextView)itemView.findViewById(R.id.EventStartTime);
            EventEndTime = (TextView)itemView.findViewById(R.id.EventEndTime);
            EventID = (TextView)itemView.findViewById(R.id.EventID);

//            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);

            EditAssignBeacon.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", events.get(getLayoutPosition()));
            Fragment fragment = new ManageEventAssignedBeaconFragment();
            fragment.setArguments(bundle);
//
            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentMainDrawer, fragment, "tag").addToBackStack(null).commit();
        }
    }


}
