package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_dashboard);

        ButterKnife.bind(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // getSupportActionBar().hide();


        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        setTitle("Welcome " + userDetails.get(SessionManager.KEY_USER_NAME));


        btnFreeCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, DashBoardActivity.class);
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
                Intent intent = new Intent(context, SecondaryDashboard.class);
                startActivity(intent);
                finish();

            }








        }



    }

}
