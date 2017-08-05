package com.myoffersapp;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.myoffersapp.fragments.FragmentProfile;
import com.myoffersapp.fragments.HomeFragment;
import com.myoffersapp.fragments.OfferHistoryFragment;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.GPSTracker;
import com.myoffersapp.model.FCMReponse;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.myoffersapp.ui.CustomTypefaceSpan;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrListener;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    private void AlertForLocationServices()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);

        builder.setMessage(getString(R.string.app_name)+" would like to use your current location to customize your experience.");


        /*builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //Toast.makeText(context , "Please enable GPS",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Enable GPS");
                //Log.d(TAG , "Device not supported SIM, Please enable GPS ");

            }
        });

        try {
            AlertDialog alert11 = builder.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            //buttonbackground.setBackgroundColor(Color.BLUE);
            buttonbackground.setTextColor(Color.parseColor("#3F51B5"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#3F51B5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //buttonbackground1.setBackgroundColor(Color.parseColor("#3F51B5"));


        //builder.show();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.LEFT).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);*/

        /*int primary = getResources().getColor(R.color.colorPrimary);
        int secondary = getResources().getColor(R.color.colorPrimaryDark);
        Slidr.attach(this, primary, secondary);*/


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


        //Set Custom font family to navigation drawer menu:
       // navView = (NavigationView) findViewById(R.id.navView);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }






        Dexter.withActivity(DashBoardActivity.this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // startService(new Intent(getBaseContext(), MyLocationService.class));
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            AlertForLocationServices();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                           permission, PermissionToken token) {/* ... */}
                }).check();


        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                getMenuInflater().inflate(R.menu.activity_dash_board_drawer, menu);
                MenuItem mProfileFrag = menu.findItem(R.id.nav_profile);

                onNavigationItemSelected(mProfileFrag);



            }
        });




       /* if (userDetails.get(SessionManager.KEY_USER_EMAIL).equals("") || userDetails.get(SessionManager.KEY_USER_ADDRESS).equals("") || userDetails.get(SessionManager.KEY_USER_NAME).equals("")) {
            setupFragment(new FragmentProfile(), "Profile");
        } else {
            setupFragment(null, getString(R.string.app_name));
        }*/
        setupFragment(null, getString(R.string.app_name));



        if (userDetails.get(SessionManager.KEY_USER_IS_FIRST_BILL).equals("1")) {

            UpdateFcmTokenDetailsToServer();

        }


    }

    private void UpdateFcmTokenDetailsToServer() {

        CommonMethods.showDialog(spotsDialog);

        String fcm_tokenid = "";
        try {
            MyFirebaseInstanceIDService mid = new MyFirebaseInstanceIDService();
            fcm_tokenid = String.valueOf(mid.onTokenRefreshNew(context));

        } catch (Exception e) {
            fcm_tokenid = "";
            e.printStackTrace();
        }



        ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG , "URL UpdateFCM Token : "+ AllKeys.WEBSITE +"type=fcmtoken?userid="+ userDetails.get(SessionManager.KEY_USER_ID) +"&token="+ fcm_tokenid +"&devicetype=android");
        apiService.sendFCMTokenToServer("fcmtoken",userDetails.get(SessionManager.KEY_USER_ID),fcm_tokenid,"android").enqueue(new Callback<FCMReponse>() {
            @Override
            public void onResponse(Call<FCMReponse> call, Response<FCMReponse> response) {

                if(response.code() != 200)
                {

                    CommonMethods.showServerError(context , response.code());
                }
                CommonMethods.hideDialog(spotsDialog);
            }

            @Override
            public void onFailure(Call<FCMReponse> call, Throwable t) {

                CommonMethods.displayFailerError(context,TAG,t);
                CommonMethods.hideDialog(spotsDialog);


            }
        });




    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/mavenpro_regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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

        SetUserProfilePictireFromBase64EnodedString();

        if (id == R.id.nav_home) {
            // Handle the camera action
            setupFragment(new HomeFragment() , "Home");
        } else if (id == R.id.nav_profile) {

            setupFragment(new FragmentProfile() , "Profile");
        }
        else if (id == R.id.nav_referal_history) {

            Intent intent = new Intent(context , ReferAndEarnTabActivity.class);
           // Intent intent = new Intent(context , AskMobileNoActivity.class);
            intent.putExtra(AllKeys.ACTIVITYNAME , TAG);
            startActivity(intent);
            finish();
        }
        else if(id == R.id.nav_offer_history)
        {


            setupFragment(new OfferHistoryFragment() , "Offer History");
        }
        else if(id == R.id.nav_nearby)
        {


            Intent intent = new Intent(context , VendorsActivity.class);
            intent.putExtra("VENDORTYPE" , "all");
            startActivity(intent);
            finish();




        }

        else if (id == R.id.nav_logout) {

            sessionManager.logoutUser();

        }
        else if (id == R.id.nav_rateapp) {




            final RatingDialog ratingDialog = new RatingDialog.Builder(context)
                    .threshold(1)
                    .session(1)
                    .ratingBarColor(R.color.yellow)
                    .playstoreUrl("https://play.google.com/store/apps/details?id="+ context.getPackageName() +"")
                    .title("If you enjoy using " + getString(R.string.app_name) + ", please take a moment to rate it. Thanks for your support!")
                    .titleTextColor(R.color.black)
                    .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                        @Override
                        public void onFormSubmitted(String feedback) {

                        }
                    }).build();

            ratingDialog.show();


           // AppRater.app_launched(this);

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
