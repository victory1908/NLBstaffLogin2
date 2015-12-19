package victory1908.nlbstafflogin2.beaconstac;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victory1908 on 16-Dec-15.
 */
public class Beacon implements Parcelable {


    public String beaconUUID;
    public String beaconMajor;
    public String beaconMinor;

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public void setBeaconUUID(String beaconUUID) {
        this.beaconUUID = beaconUUID;
    }

    public String getBeaconMajor() {
        return beaconMajor;
    }

    public void setBeaconMajor(String beaconMajor) {
        this.beaconMajor = beaconMajor;
    }

    public String getBeaconMinor() {
        return beaconMinor;
    }

    public void setBeaconMinor(String beaconMinor) {
        this.beaconMinor = beaconMinor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.beaconUUID);
        dest.writeString(this.beaconMajor);
        dest.writeString(this.beaconMinor);
    }

    public Beacon() {
    }

    protected Beacon(Parcel in) {
        this.beaconUUID = in.readString();
        this.beaconMajor = in.readString();
        this.beaconMinor = in.readString();
    }

    public static final Parcelable.Creator<Beacon> CREATOR = new Parcelable.Creator<Beacon>() {
        public Beacon createFromParcel(Parcel source) {
            return new Beacon(source);
        }

        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }
    };
}
