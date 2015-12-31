package victory1908.nlbstafflogin2;


public class Config {
    //JSON URL
    public static final String LOGIN_URL = "http://vinhvumobile.com/phpconnect/volleylogin.php";
    public static final String EVENT_URL = "http://vinhvumobile.com/phpconnect/getevent.php";
    public static final String GET_EVENT_FROM_BEACON = "http://vinhvumobile.com/phpconnect/getEventFromBeacon.php";

    public static final String DATA_URL = "http://vinhvumobile.com/phpconnect/geteventdetail.php";
    public static final String GET_ALL_EVENT_URL = "http://vinhvumobile.com/phpconnect/getAllEvent.php";


    public static final String UPDATE_EVENT_URL = "http://vinhvumobile.com/phpconnect/updateEvent.php";
    public static final String UPDATE_BEACON_URL = "http://vinhvumobile.com/phpconnect/updateBeacon.php";

    public static final String DELETE_EVENT_URL = "http://vinhvumobile.com/phpconnect/deleteEvent.php";
    public static final String DELETE_BEACON_URL = "http://vinhvumobile.com/phpconnect/deleteBeacon.php";

    public static final String CREATE_EVENT_URL = "http://vinhvumobile.com/phpconnect/createEvent.php";

    public static final String REGISTER_BEACON_URL = "http://vinhvumobile.com/phpconnect/registerBeacon.php";
    public static final String ASSIGN_BEACON_URL = "http://vinhvumobile.com/phpconnect/assignBeacon.php";



    public static final String GET_ALL_BEACON_URL = "http://vinhvumobile.com/phpconnect/getBeacon.php";

    public static final String GET_BEACON_FROM_EVENT = "http://vinhvumobile.com/phpconnect/getBeaconFromEvent.php";
    public static final String GET_AVAILABLE_BEACON = "http://vinhvumobile.com/phpconnect/getAvailableBeacon.php";



    public static final String CHECK_IN_URL = "http://vinhvumobile.com/phpconnect/checkIn.php";
    public static final String DAILY_CHECK_IN_URL = "http://vinhvumobile.com/phpconnect/dailyCheckIn.php";

    //Tags used in the JSON String
    //This would be used to store the password of current logged in user
    public static final String STAFF_ID ="StaffID";
    public static final String PASSWORD ="Password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for SharedPreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the password of current logged in user

    //We will use this to store the boolean in sharedPreference to track user is loggedin or not
    public static final String LOGGED_IN_SHARED_PREF = "loggedin";


    public static final String EVENT_ID = "EventID";

    public static final String EVENT_TITLE = "EventTitle";
    public static final String EVENT_DESC = "EventDesc";
    public static final String EVENT_START_TIME = "EventStartTime";
    public static final String EVENT_END_TIME = "EventEndTime";

    public static final String BEACON_NAME = "BeaconName";
    public static final String BEACON_ID = "BeaconID";
    public static final String BEACON_SN = "BeaconSN";
    public static final String BEACON_UUID = "BeaconUUID";

//    public static final UUID BEACON_UUID = UUID.fromString("F94DBB23-2266-7822-3782-57BEAC0952AC");

    public static final String BEACON_MAJOR = "BeaconMajor";
    public static final String BEACON_MINOR = "BeaconMinor";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}
