package victory1908.nlbstafflogin2;

public class Config {
    //JSON URL
    public static final String LOGIN_URL = "http://vinhvumobile.com/phpconnect/volleylogin.php";
    public static final String EVENT_URL = "http://vinhvumobile.com/phpconnect/getevent.php";
    public static final String DATA_URL = "http://vinhvumobile.com/phpconnect/geteventdetail.php";

    public static final String CHECK_IN_URL = "http://vinhvumobile.com/phpconnect/checkIn.php";

    //Tags used in the JSON String
    //This would be used to store the password of current logged in user
    public static final String STAFFID ="staffID";
    public static final String PASSWORD ="password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for SharedPreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the password of current logged in user

    //We will use this to store the boolean in sharedPreference to track user is loggedin or not
    public static final String LOGGED_IN_SHARED_PREF = "loggedin";


    public static final String KEY_EVENT_ID = "EventID";

    public static final String EVENT_TITLE = "EventTitle";
    public static final String EVENT_DESC = "EventDesc";
    public static final String EVENT_TIME = "EventTime";

    public static final String BEACON_UUID = "beaconUUID";

    //JSON array name
    public static final String JSON_ARRAY = "result";
}
