package victory1908.nlbstafflogin2.ManageEventBeaconFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import victory1908.nlbstafflogin2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageEventAssigned extends Fragment {


    public ManageEventAssigned() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_event_assigned, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
//        Toast.makeText(getContext(),"test2",Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    public void onPause() {
//        Toast.makeText(getContext(),"test2",Toast.LENGTH_SHORT).show();
        super.onPause();
    }
}
