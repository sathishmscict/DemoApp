package com.myoffersapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import com.myoffersapp.adapter.VendorAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.GPSTracker;
import com.myoffersapp.model.SlidersData;
import com.myoffersapp.model.VendorData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.xw.repo.BubbleSeekBar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class VendorsActivity extends AppCompatActivity {

    @BindView(R.id.rvVendors)
    RecyclerView rvVendors;

    @BindView(R.id.tvKms)
    TextView tvKms;

    @BindView(R.id.final_slider1)
    SliderLayout mDemoSlider;


    HashMap<String, String> url_maps = new HashMap<String, String>();

    private ArrayList<Integer> list_SliderId = new ArrayList<Integer>();
    private ArrayList<String> list_SliderImages = new ArrayList<String>();

    private ArrayList<String> list_SliderCategoryURL = new ArrayList<String>();



    private Context context = this;
    private String TAG = VendorsActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<VendorData.Datum> list_vendorData = new ArrayList<VendorData.Datum>();
    private String vendorType="";
    private BubbleSeekBar bubbleSeekBar3;
    private TextView tvLocation;

    Geocoder geocoder;
    List<Address> addresses;



    /*   @Override
       public void onMyLocationChange(Location location)
       {



           Location target = new Location("target");
           //new LatLng[]{POINTA, POINTB, POINTC, POINTD}
           for(LatLng point : locations) {
               target.setLatitude(point.latitude);
               target.setLongitude(point.longitude);
               if(location.distanceTo(target) < 10*1000) {
                   // bingo!
               }
           }

       }
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);

        ButterKnife.bind(this);
        //Slidr.attach(this);

        SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.LEFT).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);



        geocoder = new Geocoder(this, Locale.getDefault());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvLocation = (TextView)toolbar.findViewById(R.id.tvLocation);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }


      //  setTitle("");

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);





        Toast.makeText(context, "Tap the settings icon and change kms", Toast.LENGTH_SHORT).show();


        tvKms.setText("With in "+ userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM) +" kms");
        tvKms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showKMDialog();
            }
        });
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvVendors.setLayoutManager(lManager);



        //Handle Recyclerview click
        rvVendors.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvVendors, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(context,
                            DisplayDealsAcivity.class);
                    intent.putExtra(AllKeys.ACTIVITYNAME , TAG);
                    sessionManager.setOfferTypeId(CommonMethods.OFFER_TYPE_GENERAL);
                    sessionManager.setVendorDetails(list_vendorData.get(position).getBranchid(), list_vendorData.get(position).getVendorid());
                    intent.putExtra("ActivityName", TAG);
                    startActivity(intent);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        //Get Vendor data from server
        try {
            //Check vendor type as all/categorywised
            vendorType = getIntent().getStringExtra("VENDORTYPE");

            if(vendorType == null)
            {
                vendorType = "vendor";

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        getSlidersByTypeDetailsFromServer();
        getVendorDetailsFromServer(vendorType);



        Dexter.withActivity(VendorsActivity.this)
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


    }


    private void getSlidersByTypeDetailsFromServer() {
       // CommonMethods.showDialog(spotsDialog);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL : " + AllKeys.WEBSITE + "ViewBanner?type=vendor&bannertype=category&categoryid=0&vendorid=0");
        apiService.getSlidersDataFromServer("banner", "vendor", userDetails.get(SessionManager.KEY_CATEGORYID), "0").enqueue(new Callback<SlidersData>() {
            @Override
            public void onResponse(Call<SlidersData> call, retrofit2.Response<SlidersData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "ViewBanner API Called Success" + response.toString());


                if (response.code() == 200)
                {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false) {

                        if (record_status == true) {


                            url_maps.clear();
                            list_SliderId.clear();
                            list_SliderImages.clear();

                            list_SliderCategoryURL.clear();
                            List<SlidersData.Datum> arr = response.body().getData();

                            for (int i = 0; i < arr.size(); i++) {
                                //JSONObject c = arr.getJSONObject(i);

                                String url_image_url = arr.get(i).getImg();
                                //c.getString(AllKeys.TAG_BANNER_IMAGE_URL);

                                url_maps.put(String.valueOf(i), url_image_url);
                                list_SliderId.add(i);
                                list_SliderImages.add(url_image_url);


                            }
                            FillDashBoardSliders();



                        }
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<SlidersData> call, Throwable t) {


                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
                Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
                Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
                Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());


                if (t.getMessage().equals("timeout")) {
                    // getCategoryDetailsFromServer();
                }

                CommonMethods.hideDialog(spotsDialog);
            }
        });


    }

    private void FillDashBoardSliders() {



        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    /*.image(url_maps.get(name))*/
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            ;
            //.setOnSliderClickListener(getActivity());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            String sd2 = list_SliderImages.get(list_SliderImages.indexOf(url_maps.get(name)));


            int sd = list_SliderId.indexOf(list_SliderImages.indexOf(url_maps.get(name)));

            try {
                mDemoSlider.addSlider(textSliderView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if ((list_SliderId.get(list_SliderImages.indexOf(url_maps.get(name))) % 2) != 0) {
                mDemoSlider.addSlider(textSliderView);
            } else {
                mDemoSlider2.addSlider(textSliderView);

            }
*/

        }


      /*  for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
                 *//*   .setOnSliderClickListener(Newthis);*//*

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
            mDemoSlider2.addSlider(textSliderView);
        }*/

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);


//        mDemoSlider.addOnPageChangeListener(ge);


    }

    private void showKMDialog()
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_kilometer_range_selector);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnrange = (Button)dialog.findViewById(R.id.btnrange);

        DiscreteSeekBar seekBarkm = (DiscreteSeekBar)dialog.findViewById(R.id.seekBarkm);

        seekBarkm.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser) {


                btnrange.setText("FIND NEAR BY "+ progress +" K.M.");


                sessionManager.setDistanceInterval(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });



         bubbleSeekBar3 = (BubbleSeekBar) dialog.findViewById(R.id.demo_4_seek_bar_3);
        bubbleSeekBar3.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

                btnrange.setText("FIND NEAR BY "+ progress +" K.M.");


                sessionManager.setDistanceInterval(String.valueOf(progress));
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });


        // trigger by set progress or seek by finger
        bubbleSeekBar3.setProgress(Integer.parseInt(userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM)));

        btnrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
                dialog.dismiss();

                userDetails = sessionManager.getSessionDetails();
                tvKms.setText("With in "+ userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM) +" kms");

                getVendorDetailsFromServer(vendorType);
            }
        });

        dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT , Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
//onCreate completed


    private void AlertForLocationServices() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);

        builder.setMessage(getString(R.string.app_name) + " would like to use your current location to customize your experience.");


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


    private void getVendorDetailsFromServer(final String vendorType) {
        CommonMethods.showDialog(spotsDialog);

        Log.d(TAG, "URL : " + AllKeys.WEBSITE + "ViewVendorData?type="+ vendorType +"&categoryid=" + userDetails.get(SessionManager.KEY_CATEGORYID) + "");

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getAllVendorDetailsFromServer(vendorType, userDetails.get(SessionManager.KEY_CATEGORYID)).enqueue(new Callback<VendorData>() {
            @Override
            public void onResponse(Call<VendorData> call, retrofit2.Response<VendorData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "Response RAW : " + response.raw());


                if (response.code() == 200)
                {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    final String userMobile = null;
                    if (error_status == false) {


                        if (record_status == true) {

                            //list_vendorData = response.body().getData();

                            final List<VendorData.Datum> list_vendorDataTemp = response.body().getData();


                            try {


                                Dexter.withActivity(VendorsActivity.this)
                                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                        .withListener(new PermissionListener() {
                                            @Override
                                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                                // startService(new Intent(getBaseContext(), MyLocationService.class));
                                                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                                                    // create class object
                                                    GPSTracker gps = new GPSTracker(VendorsActivity.this);

                                                    Location myLocation = gps.getLocation();

                                                    // check if GPS enabled
                                                    if (gps.canGetLocation()) {

                                                        double latitude = gps.getLatitude();

                                                        double longitude = gps.getLongitude();

                                                        try {
                                                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                                            String city = addresses.get(0).getLocality();
                                                            String state = addresses.get(0).getAdminArea();
                                                            String country = addresses.get(0).getCountryName();
                                                            String postalCode = addresses.get(0).getPostalCode();
                                                            String knownName = addresses.get(0).getFeatureName();

                                                            tvLocation.setText(addresses.get(0).getSubLocality());

                                                        } catch (IOException e) {
                                                            tvLocation.setText("");
                                                            e.printStackTrace();
                                                        }



                                                        // \n is for new line
                                                       // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                                        Log.d(TAG , "Current Location : "+"Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
                                                    } else {
                                                        // can't get location
                                                        // GPS or Network is not enabled
                                                        // Ask user to enable GPS/network in settings
                                                        gps.showSettingsAlert();
                                                    }


                                                    Location target = new Location("target");
                                                    list_vendorData.clear();
                                                    for (int i = 0; i < list_vendorDataTemp.size(); i++) {

                                                        target.setLatitude(Double.parseDouble(list_vendorDataTemp.get(i).getLatitude()));
                                                        target.setLongitude(Double.parseDouble(list_vendorDataTemp.get(i).getLongitude()));
                                                        Log.d(TAG, "Name  : " + list_vendorDataTemp.get(i).getName() + " : " + list_vendorDataTemp.get(i).getAddress() + " : Location in Distance : " + myLocation.distanceTo(target));

                                                        Log.d(TAG,"Current Distance  : "+Integer.parseInt(userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM)));
                                                        if (myLocation.distanceTo(target) < Integer.parseInt(userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM)) * 1000) {
                                                            // bingo!
                                                            Log.d(TAG, "Within a 10 km " + list_vendorDataTemp.get(i).getName());
                                                            //list_vendorData.remove(i);
                                                            list_vendorData.add(list_vendorDataTemp.get(i));
                                                        } else {
                                                            Log.d(TAG, "Not Within a 10 km " + list_vendorDataTemp.get(i).getName());


                                                        }


                                                    }

                                                    VendorAdapterRecyclerView adapter = new VendorAdapterRecyclerView(context, list_vendorData,vendorType);
                                                    rvVendors.setAdapter(adapter);



                                                }





                                            }

                                            @Override
                                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                            @Override
                                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                                   permission, PermissionToken token) {/* ... */}
                                        }).check();





                             /*   Location location = new Location(LocationManager.GPS_PROVIDER);
                                Log.d(TAG , "Lattitude : " + location.getLatitude());
                                Log.d(TAG , "Longtitude : " + location.getLongitude());

                                location.setLatitude(location.getLatitude());
                                location.setLongitude(location.getLongitude());
                                Location target = new Location("target");

                                target.setLatitude(location.getLatitude());
                                target.setLongitude(location.getLongitude());
                                if(location.distanceTo(target) < 10*1000) {
                                    // bingo!
                                    Log.d(TAG , "Within a 10 km ");
                                }
                                else
                                {
                                    Log.d(TAG , "Noy Within a 10 km ");

                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //new LatLng[]{POINTA, POINTB, POINTC, POINTD}
                         /*   for(LatLng point : location)
                            {
                                Location target = new Location("target");

                                target.setLatitude(location.latitude);
                                target.setLongitude(location.longitude);
                                if(location.distanceTo(target) < 10*1000) {
                                    // bingo!
                                    Log.d(TAG , "Within a 10 km ");
                                }
                                else
                                {
                                    Log.d(TAG , "Noy Within a 10 km ");

                                }
                            }*/


                        }
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<VendorData> call, Throwable t) {

                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_vendor, menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();

        }
        else if(item.getItemId() == R.id.menu_location)
        {
           // Toast.makeText(context, "Dialog for GPS Distance", Toast.LENGTH_SHORT).show();
            try {
                Log.d(TAG, "Clicked on Location Filter");


                //   Toast.makeText(context, "Filter", Toast.LENGTH_SHORT).show();


                //mSweetSheet3.toggle();

               showKMDialog();





            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

}
