package victory1908.nlbstafflogin2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText editTextStaffID;
    private EditText editTextPassword;
    private Button buttonLogin;

    String staffID;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(loggedIn){
            //We will start the Main Activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        }

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

        buttonLogin.setOnClickListener(this);

//        //Set buttonOnClick
//        buttonLogin.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //If we will get true
        if(loggedIn){
            //We will start the Main Activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        }
    }


     public void userLogin() {
         final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_Login);
         progressBar.setVisibility(View.VISIBLE);


         //        Getting values from edit texts
         staffID = editTextStaffID.getText().toString().trim();
         password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        progressBar.setVisibility(View.GONE);

                        if(response.trim().equals(Config.LOGIN_SUCCESS)){

                            loggedIn = true;
                            editor.putBoolean(Config.LOGGED_IN_SHARED_PREF, true);
                            editor.putString(Config.STAFF_ID, staffID);
                            editor.putString(Config.PASSWORD, password);

                            //Saving values to editor
                            editor.apply();

                            //Starting Main activity
                            Intent actA = new Intent(LoginActivity.this, MainActivity.class);
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
                        progressBar.setVisibility(View.GONE);
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
    public void onClick(View view) {
        if (view==buttonLogin) userLogin();
    }


    @Override
    public void onBackPressed() {
        exit();
    }

}
