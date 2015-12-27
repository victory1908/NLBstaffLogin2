package victory1908.nlbstafflogin2.beaconstac;

import android.content.Context;

import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by Victory1908 on 22-Dec-15.
 */
public class beaconAdapterTest extends BeaconstacReceiver {
    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> arrayList) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon msBeacon) {

    }

    @Override
    public void exitedBeacon(Context context, MSBeacon msBeacon) {

    }

    @Override
    public void triggeredRule(Context context, String s, ArrayList<MSAction> arrayList) {

    }

    @Override
    public void enteredRegion(Context context, String s) {

    }

    @Override
    public void exitedRegion(Context context, String s) {

    }

    @Override
    public void enteredGeofence(Context context, ArrayList<MSPlace> arrayList) {

    }

    @Override
    public void exitedGeofence(Context context, ArrayList<MSPlace> arrayList) {

    }
}
