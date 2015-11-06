package victory1908.nlbstafflogin2;

/**
 * Created by Victory1908 on 04-Nov-15.
 */
public class Config {
    //JSON URL
    public static final String DATA_URL = "http://simplifiedcoding.16mb.com/Spinner/json.php";
    public static final String EVENT_URL = "http://vinhvumobile.com/phpconnect/getevent.php";
    public static final String CHECK_IN_URL = "http://vinhvumobile.com/phpconnect/checkIn.php";

    //Tags used in the JSON String
    public static final String KEY_EVENT_ID = "EventID";

    public static final String KEY_BEACON_UUID = "beaconUUID";
    public String beaconUUID = "F94DBB23-2266-7822-3782-57BEAC0952AC";
    public static final String TAG_NAME = "name";
    public static final String TAG_COURSE = "course";
    public static final String TAG_SESSION = "session";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}
