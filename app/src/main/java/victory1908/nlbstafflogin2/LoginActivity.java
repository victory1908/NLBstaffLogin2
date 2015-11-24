package victory1908.nlbstafflogin2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{



    private EditText editTextStaffID;
    private EditText editTextPassword;
    private Button buttonLogin;

    public static String staffID;
    public static String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextStaffID = (EditText) findViewById(R.id.editTextStaffID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);

    }


    private void userLogin() {
        staffID = editTextStaffID.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            beacon_main();
                        }else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString()+ "wrong StaffID or password",Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(Config.KEY_STAFFID,staffID);
                map.put(Config.KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void beacon_main(){
        Intent intent = new Intent(this, Beacon_MainActivity.class);
        intent.putExtra(Config.KEY_STAFFID, staffID);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }
}
