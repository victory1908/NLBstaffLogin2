package victory1908.nlbstafflogin2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import victory1908.nlbstafflogin2.beaconstac.Beacon_MainActivity;


public class LoginActivity extends BaseActivity implements View.OnClickListener{



    private EditText editTextStaffID;
    private EditText editTextPassword;
    private Button buttonLogin;

    String staffID;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLBstaffAttedance");
        toolbar.setLogo(R.drawable.nlblogo);


        editTextStaffID = (EditText) findViewById(R.id.editTextStaffID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

//        Getting values from edit texts
        staffID = editTextStaffID.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        boolean loggedIn;
        super.onResume();
//        //In onResume fetching value from sharedPreference
//        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
//
//        //Fetching the boolean value form sharedPreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGED_IN_SHARED_PREF, false);
//
        //If we will get true
        if(loggedIn){
            //We will start the Beacon_Main Activity
            Intent intent = new Intent(LoginActivity.this, Beacon_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        }
    }


     void userLogin() {

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
//                            //Creating a shared preference
//                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//
//                            //Creating editor to store values to shared preferences
//                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGED_IN_SHARED_PREF, true);
                            editor.putString(Config.KEY_STAFFID, staffID);
                            editor.putString(Config.KEY_PASSWORD, password);


                            //Saving values to editor
                            editor.apply();

                            //Starting Beacon_Main activity
                            Intent actA = new Intent(LoginActivity.this, Beacon_MainActivity.class);
                            actA.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(actA);
                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this,response +"invalid StaffID or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.KEY_STAFFID,password);
                map.put(Config.KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void beacon_main(){
//        Intent intent = new Intent(this, Beacon_MainActivity.class);
//        intent.putExtra(Config.KEY_STAFFID, staffID);
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
