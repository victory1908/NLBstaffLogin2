package victory1908.nlbstafflogin2;

import android.content.Intent;
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

        //Casting EditText and Button
        editTextStaffID = (EditText) findViewById(R.id.editTextStaffID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        //Set buttonOnClick
        buttonLogin.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //If we will get true
        if(loggedIn){
            //We will start the Beacon_Main Activity
            Intent intent = new Intent(LoginActivity.this, Beacon_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        }
    }


     private void userLogin() {

         //        Getting values from edit texts
         staffID = editTextStaffID.getText().toString().trim();
         password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.trim().equals(Config.LOGIN_SUCCESS)){

                            editor.putBoolean(Config.LOGGED_IN_SHARED_PREF, true);
                            editor.putString(Config.STAFF_ID, staffID);
                            editor.putString(Config.PASSWORD, password);

                            //Saving values to editor
                            editor.apply();

                            //Starting Beacon_Main activity
                            Intent actA = new Intent(LoginActivity.this, Beacon_MainActivity.class);
                            actA.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(actA);
                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this,response +" invalid StaffID or Password",Toast.LENGTH_SHORT).show();
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
                map.put(Config.STAFF_ID,staffID);
                map.put(Config.PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        userLogin();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

}
