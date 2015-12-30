package victory1908.nlbstafflogin2.beaconstac;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Victory1908 on 16-Dec-15.
 */
public class Beacon implements Parcelable {
    private String beaconName, beaconID, beaconSN, beaconUUID;
    private int major,minor;
    private boolean isSelected;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }

    public String getBeaconSN() {
        return beaconSN;
    }

    public void setBeaconSN(String beaconSN) {
        this.beaconSN = beaconSN;
    }

    public String getBeaconUUID() {
        return beaconUUID;
    }

    public void setBeaconUUID(String beaconUUID) {
        this.beaconUUID = beaconUUID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.beaconName);
        dest.writeString(this.beaconID);
        dest.writeString(this.beaconSN);
        dest.writeString(this.beaconUUID);
        dest.writeInt(this.major);
        dest.writeInt(this.minor);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }


    public Beacon() {
    }

    protected Beacon(Parcel in) {
        this.beaconName = in.readString();
        this.beaconID = in.readString();
        this.beaconSN = in.readString();
        this.beaconUUID = in.readString();
        this.major = in.readInt();
        this.minor = in.readInt();
        this.isSelected = in.readByte() != 0;
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

