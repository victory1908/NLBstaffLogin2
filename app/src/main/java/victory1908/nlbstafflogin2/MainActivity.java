package victory1908.nlbstafflogin2;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import victory1908.nlbstafflogin2.ManageBeaconFragment.EditRegisteredBeacon;
import victory1908.nlbstafflogin2.ManageBeaconFragment.ManageBeaconFragment;
import victory1908.nlbstafflogin2.ManageEventBeaconFragment.ManageRule;
import victory1908.nlbstafflogin2.ManageEventFragment.EditEventFragment;
import victory1908.nlbstafflogin2.ManageEventFragment.ManageEventFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener  {



    //    private Button buttonRegister;
    private Button buttonLogin;

    public static boolean exit = false;

    //handle exit

    public static final int REQUEST_CODE = 0;
//    public static final int RESULT_CODE = 0;
    public static final String EXTRA_EXIT = "exit";


    TextView staffID;
    Menu body;

    Boolean homeFlag =true;
    Fragment fragment = new Fragment();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nlbattendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("NLB-StaffAttendance");
        toolbar.setLogo(R.drawable.nlblogo);

        if (!loggedIn) {
            //We will start the Login Activity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
//
        staffID = (TextView)header.findViewById(R.id.StaffID);
//
        staffID.setText(sharedPreferences.getString(Config.STAFF_ID, "Not Available"));

        body = navigationView.getMenu();

        //View admin console
        if (staffID.getText().toString().contains("admin")) {
            body.findItem(R.id.manageBeacon).setVisible(true);
            body.findItem(R.id.manageEvent).setVisible(true);
            body.findItem(R.id.assignEventToBeaconMenu).setVisible(true);
        }else {
            body.findItem(R.id.manageBeacon).setVisible(false);
            body.findItem(R.id.manageEvent).setVisible(false);
            body.findItem(R.id.assignEventToBeaconMenu).setVisible(false);
        }


    }

    // end oncreate




    @Override
    public void onClick(View view) {

//        Intent actLogin = new Intent(this, LoginActivity.class);
//        startActivityForResult(actLogin, REQUEST_CODE);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!homeFlag){
                fragment = new MainFragment();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.contentMainDrawer, fragment).commit();
                homeFlag = true;
            }else exit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.scan_beacon:
                fragment = new Beacon_MainFragment();
                homeFlag = true;
                break;
            case R.id.manageBeacon:
//                fragment = new ManageBeaconFragment();
                fragment = new ManageBeaconFragment();
                homeFlag = false;
                break;
            case R.id.manageEvent:
                fragment = new ManageEventFragment();
                homeFlag = false;
                break;
            case R.id.assignEventToBeaconMenu:
                fragment = new ManageRule();
                homeFlag = false;
                break;
            case R.id.action_logout:
                logout();
                break;
        }

        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.contentMainDrawer,fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
