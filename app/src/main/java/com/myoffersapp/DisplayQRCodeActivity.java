package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.utils.Contents;
import com.myoffersapp.utils.QRCodeEncoder;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class DisplayQRCodeActivity extends AppCompatActivity {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;

    @BindView(R.id.tvOfferTitle)
    TextView tvOfferTitle;

    @BindView(R.id.tvVendorName)
    TextView tvVendorName;

    @BindView(R.id.tvVendorAddress)
    TextView tvVendorAddress;


    private Context context=this;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private String TAG = DisplayDealsAcivity.class.getSimpleName();
    private String json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qrcode);

        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.VERTICAL).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setTitle("Deal QR-CODE ");

        spotsDialog = new SpotsDialog(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvOfferTitle.setText(userDetails.get(SessionManager.KEY_OFFER_TITLE));
        tvVendorAddress.setText(userDetails.get(SessionManager.KEY_VENDOR_ADDRESS));
        tvVendorName.setText(userDetails.get(SessionManager.KEY_BRANCHNAME));


        //Encode with a QR Code image

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;



        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("offerid", userDetails.get(SessionManager.KEY_OFFERID));
            jsonObject.accumulate("branchid", userDetails.get(SessionManager.KEY_BRANCHIID));
            jsonObject.accumulate("userid", userDetails.get(SessionManager.KEY_USER_ID));

            json = json + jsonObject.toString() ;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String urlSingleOffer = json;
        Log.d(TAG  , " Single Offer URL : "+urlSingleOffer);
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(urlSingleOffer,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

            ivQRCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, SingleDealDispalyActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, SingleDealDispalyActivity.class);
        startActivity(intent);
        finish();
    }

}
