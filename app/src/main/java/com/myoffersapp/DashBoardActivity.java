package com.myoffersapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.myoffersapp.fragments.HomeFragment;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;

import java.util.HashMap;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = DashBoardActivity.class.getSimpleName();
    private TextView tvUserName;
    private TextView tvEmail;
    private ImageView imgProfilePic;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        try {
            Log.d(TAG, "DEVICE ID : " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setVisibility(View.VISIBLE);


        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_dash_board);

        tvUserName = (TextView) headerLayout.findViewById(R.id.tvName);
        tvEmail = (TextView) headerLayout.findViewById(R.id.tvEmail);
        imgProfilePic = (ImageView) headerLayout.findViewById(R.id.imgProfilePic);



        SetUserProfilePictireFromBase64EnodedString();

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    /*Fragment fragment = new FragmentProfile();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();


                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);*/


                getMenuInflater().inflate(R.menu.activity_dash_board_drawer, menu);
                MenuItem mProfileFrag = menu.findItem(R.id.nav_profile);

                onNavigationItemSelected(mProfileFrag);


                    /*MenuItem mDefaultFrag = (MenuItem) navigationView.findViewById(R.id.nav_profile);
                    onNavigationItemSelected(mDefaultFrag);*/


            }
        });




       /* if (userDetails.get(SessionManager.KEY_USER_EMAIL).equals("") || userDetails.get(SessionManager.KEY_USER_ADDRESS).equals("") || userDetails.get(SessionManager.KEY_USER_NAME).equals("")) {
            setupFragment(new FragmentProfile(), "Profile");
        } else {
            setupFragment(null, getString(R.string.app_name));
        }*/
        setupFragment(null, getString(R.string.app_name));


    }

    private void UpdateFcmTokenDetailsToServer() {


    }


    private void SetUserProfilePictireFromBase64EnodedString() {

        CommonMethods.showDialog(spotsDialog);

        tvUserName.setText(userDetails.get(SessionManager.KEY_USER_NAME));
        tvEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));

        try {
            userDetails = sessionManager.getSessionDetails();
            String myBase64Image = userDetails.get(SessionManager.KEY_ENODEDED_STRING);
            if (!myBase64Image.equals("")) {

                Bitmap myBitmapAgain = CommonMethods.decodeBase64(myBase64Image);

                imgProfilePic.setImageBitmap(myBitmapAgain);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Decode Img Exception : ", e.getMessage());
            CommonMethods.hideDialog(spotsDialog);
        }
        CommonMethods.hideDialog(spotsDialog);
    }

    public void setupFragment(Fragment fragment, String title) {
        setTitle(title);

        if (fragment != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();


        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
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

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {

            sessionManager.logoutUser();

        }
        else if (id == R.id.nav_rateapp) {

            AppRater.app_launched(this);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class AppRater
    {

        private final static String APP_TITLE = "Offers App";// App Name
        private final static String APP_PNAME = "com.myoffersapp";// Package Name

        private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
        private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches


        public static void app_launched(Context mContext) {
            SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
            boolean vv = prefs.getBoolean("dontshowagain", false);

            if (prefs.getBoolean("dontshowagain", false)) {
                Log.d("dontshowagain", "FALSE");
                return;
            }
            /*if (prefs.getBoolean("dontshowagain", true)) {
                Log.d("dontshowagain", "TRUE");

                return;

            }*/

            SharedPreferences.Editor editor = prefs.edit();

            // Increment launch counter
            long launch_count = prefs.getLong("launch_count", 0) + 1;
            editor.putLong("launch_count", launch_count);

            // Get date of first launch
            Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
            if (date_firstLaunch == 0) {
                showRateDialog(mContext, editor);
                date_firstLaunch = System.currentTimeMillis();
                editor.putLong("date_firstlaunch", date_firstLaunch);
            }
            //showRateDialog(mContext, editor);
            // Wait at least n days before opening
            if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
                if (System.currentTimeMillis() >= date_firstLaunch +
                        (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                    showRateDialog(mContext, editor);
                }
            }

            editor.commit();
        }

        public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {


            final Dialog dialog = new Dialog(mContext);


            dialog.setContentView(R.layout.dialog_rateapp);

            dialog.setTitle("Rate " + APP_TITLE);

            TextView txvRateApp = (TextView) dialog.findViewById(R.id.txtRateApp);
            TextView txvTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            TextView txvRemindMeLater = (TextView) dialog.findViewById(R.id.txtRemidMeLater);
            TextView txvNoThanks = (TextView) dialog.findViewById(R.id.txtNoThanks);


            txvTitle.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
            txvRateApp.setText("Rate "+ APP_TITLE +"");
            txvRateApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editor != null) {
                        editor.putBoolean("dontshowagain", false);
                        editor.commit();
                    }


                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                    dialog.dismiss();

                }
            });
            txvRemindMeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
            txvNoThanks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });


            dialog.show();
        }
    }
}
