package victory1908.nlbstafflogin2;

/**
 * Created by Victory1908 on 04-Nov-15.
 */
public class Config {
    //JSON URL
    public static final String EVENT_URL = "http://vinhvumobile.com/phpconnect/getevent.php";
    public static final String DATA_URL = "http://vinhvumobile.com/phpconnect/geteventdetail.php";

    public static final String CHECK_IN_URL = "http://vinhvumobile.com/phpconnect/checkIn.php";

    //Tags used in the JSON String
    public static final String KEY_EVENT_ID = "EventID";

    public static final String EVENT_TITLE = "EventTitle";
    public static final String EVENT_DESC = "EventDesc";
    public static final String EVENT_TIME = "EventTime";

    public static final String KEY_BEACON_UUID = "beaconUUID";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}
