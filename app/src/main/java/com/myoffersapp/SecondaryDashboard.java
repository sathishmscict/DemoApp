package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.FCMReponse;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondaryDashboard extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnFreeCoupon)
    Button btnFreeCoupon;
    @BindView(R.id.btnOffers)
    Button btnOffers;
    @BindView(R.id.btnRefer)
    Button btnRefer;


    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = SecondaryDashboard.class.getSimpleName();
    private SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_dashboard);

        ButterKnife.bind(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().hide();
        SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.LEFT).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);



        setTitle("Welcome " + userDetails.get(SessionManager.KEY_USER_NAME));



      /*  Intent intentt = new Intent(context, SingleDealDispalyActivity.class);
        startActivity(intentt);
        finish();*/

        btnFreeCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sessionManager.setOfferTypeId(CommonMethods.OFFER_TYPE_REFERAL);
                Intent intent = new Intent(context, DisplayDealsAcivity.class);
                intent.putExtra(AllKeys.ACTIVITYNAME , TAG);
                startActivity(intent);
                finish();

            }
        });


        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ReferAndEarnTabActivity.class);
                intent.putExtra(AllKeys.ACTIVITYNAME , TAG);
                startActivity(intent);
                finish();

            }
        });


        if (userDetails.get(SessionManager.KEY_USER_MOBILE).length() == 10 && userDetails.get(SessionManager.KEY_USER_VERIFICATION_STATUS).equals("0")) {
            Intent intent = new Intent(context, VerificationActivity.class);
            startActivity(intent);
            finish();

        } else if (userDetails.get(SessionManager.KEY_USER_ID).equals("0")) {

            Intent intent = new Intent(context, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {


            //Check User has places first bill or claim first offer, if not claimed first offer and can't placed first bill then rediect to seconday dashboard
            if (userDetails.get(SessionManager.KEY_USER_IS_FIRST_BILL).equals("1")) {
                Intent intent = new Intent(context, DashBoardActivity.class);
                startActivity(intent);
                finish();

            }
            else
            {
                UpdateFcmTokenDetailsToServer();

            }


        }



    }
    //omCreate completed

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

                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();
                    String isFirstBillPlaced = response.body().getISFIRSTBILL();
                    CommonMethods.hideDialog(spotsDialog);
                    if(isFirstBillPlaced.equals("1"))
                    {
                        Intent intent = new Intent(context , DashBoardActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }
                else
                {
                    Toast.makeText(SecondaryDashboard.this, getString(R.string.server_error)+response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<FCMReponse> call, Throwable t) {

                Toast.makeText(SecondaryDashboard.this, getString(R.string.api_error)+t.getMessage(), Toast.LENGTH_LONG).show();


                Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
                Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
                Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
                Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());

                Toast.makeText(context, "Sorry , try again...", Toast.LENGTH_SHORT).show();

                if(t.getMessage().equals("timeout"))
                {
                    // sendReviewDetailsToServer();
                }

                CommonMethods.hideDialog(spotsDialog);

            }
        });




    }

}
