package victory1908.nlbstafflogin2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


//    @Override
//    public void onBackPressed() {
//        exit();
//    }
//
//    //exit function
//    private void exit(){
//        //Creating an alert dialog to confirm exit
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are you sure you want to exit?");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        finish();
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                    }
//                });
//        //Showing the alert dialog
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }

    private static final String REGISTER_URL = "http://simplifiedcoding.16mb.com/UserRegistration/volleyRegister.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    //    private Button buttonRegister;
    private Button buttonLogin;

    public static boolean exit = false;

    //handle exit

    public static final int REQUEST_CODE = 0;
//    public static final int RESULT_CODE = 0;
    public static final String EXTRA_EXIT = "exit";

    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLB-StaffAttendance");
        toolbar.setLogo(R.drawable.nlblogo);

            //We will start the Login Activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.package.ACTION_EXIT");
//        registerReceiver(myBroadcastReceiver, intentFilter);

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("onReceive", "Logout in progress");
//                finish();
//            }
//        }, intentFilter);


//        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
//        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        editTextEmail= (EditText) findViewById(R.id.editTextEmail);

//        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

//        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    // end oncreate


    //BroadcastReceiver

//    private void registerReceiver(){
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.package.ACTION_EXIT");
//        registerReceiver(myBroadcastReceiver, intentFilter);
//    }
//
//    @Override
//    public void onResume() {
//        registerReceiver();
//        super.onResume();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        registerReceiver();
////        unregisterReceiver(myBroadcastReceiver);
//    }
//
//    BroadcastReceiver myBroadcastReceiver =
//            new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    Log.d("onReceive", "Logout in progress");
//                    Toast.makeText(MainActivity.this, "what the fuck", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            };


//    private void registerUser(){
//        final String username = editTextUsername.getText().toString().trim();
//        final String password = editTextPassword.getText().toString().trim();
//        final String email = editTextEmail.getText().toString().trim();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<>();
//                params.put(KEY_USERNAME,username);
//                params.put(PASSWORD,password);
//                params.put(KEY_EMAIL, email);
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    @Override
    public void onClick(View v) {
//        if(v == buttonRegister){
//            registerUser();
//        }
//        if(v == buttonLogin){

        Intent actLogin = new Intent(this, LoginActivity.class);
        startActivityForResult(actLogin, REQUEST_CODE);

//        }
    }

    //handle exit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE :
                if (data == null) {
                    return;
                }

                boolean shouldExit = data.getBooleanExtra(EXTRA_EXIT, false);
                if (shouldExit) {
                    finish();
                }
                break;
        }
    }
}
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            clearSharePreferences();
////            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void clearSharePreferences() {
//        //Getting out sharedPreferences
//        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        //Getting editor
//        SharedPreferences.Editor editor = preferences.edit();
//
//        //Puting the value false for logged_in
//        editor.putBoolean(Config.LOGGED_IN_SHARED_PREF, false);
//
//        //Putting blank value to password
//        editor.putString(Config.PASSWORD, "");
//
//        //Saving the SharedPreferences
//        editor.apply();
//    }


//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.Menu;
//import android.view.MenuItem;
//
//public class Beacon_MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
