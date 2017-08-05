package com.myoffersapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.myoffersapp.adapter.CategoryAdapterRecyclerView;
import com.myoffersapp.adapter.DealsAdapterRecyclerView;
import com.myoffersapp.animation.PulseAnimation;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.ReviewReponse;
import com.myoffersapp.model.SingleOfferData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class SingleDealDispalyActivity extends AppCompatActivity {



    @BindView(R.id.btnUseThisOffer)
    Button btnUseThisOffer;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvOfferTitle)
    TextView tvOfferTitle;

    @BindView(R.id.tvUnitPrice)
    TextView tvUnitPrice;

    @BindView(R.id.ivItem)
    ImageView ivItem;

    @BindView(R.id.tvTermsAndConditions)
    TextView tvTermsAndConditions;

    @BindView(R.id.ivCall)
    ImageView ivCall;

    @BindView(R.id.ivMap)
    ImageView ivMap;

    @BindView(R.id.ivReview)
    ImageView ivReview;

    @BindView(R.id.ivShare)
    ImageView ivShare;

    @BindView(R.id.tvKms)
    TextView tvKms;





    private Context context=this;
    private SpotsDialog spotsDialog;
    private String TAG = SingleDealDispalyActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private List<SingleOfferData.Datum> list_dealsData= new ArrayList<SingleOfferData.Datum>();

    private long DELAY_MILLIS = 1500;
    private String MOBILENO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_deal_dispaly);

        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.LEFT).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);






        setTitle("Single Deal Display");

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog =  new SpotsDialog(context);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnUseThisOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(context , DisplayQRCodeActivity.class);
                startActivity(intent);
                finish();

            }
        });


        getSingleOfferDetailsFromServer();


        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                initPulse(ivCall);

                new Handler().postDelayed(new Runnable() {


        /*      Showing splash screen with a timer. This will be useful when you
             want to show case your app logo / company*/


                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity


                        Dexter.withActivity(SingleDealDispalyActivity.this)
                                .withPermission(Manifest.permission.READ_SMS)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {

                                        if(MOBILENO.isEmpty())
                                        {

                                            Toast.makeText(context, "Sorry, mobile no not exist...", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91" + MOBILENO));
                                        context.startActivity(intent);

                                        }


                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                                }).check();


                    }
                }, DELAY_MILLIS);

            }
        });

        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPulse(ivMap);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            //sessionManager.setGPSLocations(fd.getLattitude(), fd.getLongtitude(), "");


                            //Toast.makeText(activity, "Lattitude : " + fd.getLattitude() + " Longtitude : " + fd.getLongtitude(), Toast.LENGTH_SHORT).show();

                            // sessionManager.setGPSLocations("21.2049887", "72.8385114", fd.getClientname());

                            Intent intent = new Intent(context, ContactUsActivity.class);
                            context.startActivity(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, DELAY_MILLIS);

            }
        });

        ivReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPulse(ivReview);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {




                        Intent intent = new Intent(context , OfferReviewActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, DELAY_MILLIS);







            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPulse(ivShare);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            String shareDescr = ""+ tvOfferTitle.getText().toString() +".\n" +
                                    "---- Download "+ getString(R.string.app_name) +" ----\n" +
                                    "https://play.google.com/store/apps/details?id="+ context.getPackageName() +"\n" +
                                    "Stay tuned and get Best Discounts.";




                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    shareDescr);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);




                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, DELAY_MILLIS);









            }
        });









    }


    //onCreate completed

    private void initPulse(ImageView iv) {

        PulseAnimation.create().with(iv)
                .setDuration(1500)
                .setRepeatCount(1)
                .start();


    }



    private void getSingleOfferDetailsFromServer() {


        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL ViewSingleOfferData : " + AllKeys.WEBSITE + "ViewSingleOfferData?type=offer&offerid=" + userDetails.get(SessionManager.KEY_OFFERID)  +"");
        apiService.getSingleDealsDetailsFromServer("offer",  userDetails.get(SessionManager.KEY_OFFERID)).enqueue(new Callback<SingleOfferData>() {
            @Override
            public void onResponse(Call<SingleOfferData> call, retrofit2.Response<SingleOfferData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.raw().body().toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    String userMobile = null;
                    if (error_status == false) {

                        list_dealsData.clear();
                        if (record_status == true)
                        {

                            list_dealsData = response.body().getData();

                            String descr = list_dealsData.get(0).getDescription();
                            Log.d(TAG , "Descr Before : "+descr);

                             descr = descr.replace(";", "\n\n");
                            Log.d(TAG , "Descr After : "+descr);

                            sessionManager.setOfferDetails(list_dealsData.get(0).getOfferid(),list_dealsData.get(0).getTitle());


                            MOBILENO = list_dealsData.get(0).getMobile();


                            tvDescription.setText(descr);
                            descr = list_dealsData.get(0).getTermsandcondition();
                            descr = descr.replace(";", "\n\n");
                            tvTermsAndConditions.setText("\n"+Html.fromHtml(descr));
                            tvOfferTitle.setText(list_dealsData.get(0).getTitle());
                            tvUnitPrice.setText("\u20b9 "+list_dealsData.get(0).getUnitprice());
                            CommonMethods.hideDialog(spotsDialog);

                            //Load and item image

                            try {
                                //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

                                Picasso.with(context)
                                        .load(list_dealsData.get(0).getImg())
                    .placeholder(R.drawable.bg5)
                    .error(R.drawable.bg5)
                                        .into(ivItem);




                            } catch (Exception e) {
                                e.printStackTrace();
                            }




                        }

                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<SingleOfferData> call, Throwable t) {

                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, DisplayDealsAcivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, DisplayDealsAcivity.class);
        startActivity(intent);
        finish();
    }

}
