package victory1908.nlbstafflogin2.beaconstac;

import android.os.Parcel;
import android.os.Parcelable;

import com.mobstac.beaconstac.models.MSBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import victory1908.nlbstafflogin2.Config;

/**
 * Created by Victory1908 on 16-Dec-15.
 */
public class BeaconTest extends MSBeacon implements Parcelable {
    private String beaconSN;
    private boolean isSelected;
    private String mBeaconKey;
    private String name;
    private String beaconID;
    private UUID mBeaconUUID;
    private int mMajor;
    private int mMinor;
    private int placeId;
    private int mBias;
    private boolean mIsCampedOn;

    private Parcel parcel;

    public String getBeaconSN() {
        return beaconSN;
    }

    public void setBeaconSN(String beaconSN) {
        this.beaconSN = beaconSN;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static List<BeaconTest> fromJson (JSONArray jsonArray){
        List<BeaconTest> beacons = new ArrayList<>();
        //Traversing through all the items in the json array
        for (int i = 0; i < jsonArray.length(); i++) {
            BeaconTest beacon = new BeaconTest(Parcel.obtain());
            try {


                    //Getting json object
                    JSONObject json = jsonArray.getJSONObject(i);

                    beacon.setName(json.getString(Config.BEACON_NAME));
                    beacon.beaconID = json.getString(Config.BEACON_ID);
                    beacon.setBeaconSN(json.getString(Config.BEACON_SN));
                    beacon.mBeaconUUID = UUID.fromString(json.getString(Config.BEACON_UUID));
                    beacon.mMajor= Integer.valueOf(json.getString(Config.BEACON_MAJOR));
                    beacon.mMinor = Integer.valueOf(json.getString(Config.BEACON_MINOR));

                    beacons.add(beacon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return beacons;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.beaconSN);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }



    protected BeaconTest(Parcel in) {
        super(in);
        this.beaconSN = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<BeaconTest> CREATOR = new Creator<BeaconTest>() {
        public BeaconTest createFromParcel(Parcel source) {
            return new BeaconTest(source);
        }

        public BeaconTest[] newArray(int size) {
            return new BeaconTest[size];
        }
    };
}

