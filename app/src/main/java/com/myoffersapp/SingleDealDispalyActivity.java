package com.myoffersapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.myoffersapp.model.BookmarkResponse;
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
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.ivBookmark)
    ImageView ivBookmark;

   /* @BindView(R.id.ivShare)
    ImageView ivShare;*/

    @BindView(R.id.tvCommonRating)
    TextView tvCommonRating;


    @BindView(R.id.llCall)
    LinearLayout llCall;

    @BindView(R.id.llMap)
    LinearLayout llMap;

    @BindView(R.id.llReview)
    LinearLayout llReview;

    @BindView(R.id.llBookmark)
    LinearLayout llBookmark;


    private Context context = this;
    private SpotsDialog spotsDialog;
    private String TAG = SingleDealDispalyActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<SingleOfferData.Datum> list_dealsData = new ArrayList<SingleOfferData.Datum>();

    private long DELAY_MILLIS = 1500;
    private String MOBILENO;
    private Menu menu;
    private MenuItem menu_share;

    int TOTAL_REVIEW = 0, TOTAL_BOOKMARKS = 0, TOTAL_USAGE = 0;


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

        spotsDialog = new SpotsDialog(context);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
            // getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnUseThisOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(context, DisplayQRCodeActivity.class);
                startActivity(intent);
                finish();

            }
        });


        getSingleOfferDetailsFromServer();


        llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivCall.performClick();
            }
        });
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

                                        if (MOBILENO.isEmpty()) {

                                            Toast.makeText(context, "Sorry, mobile no not exist...", Toast.LENGTH_SHORT).show();
                                        } else {
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

        llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivMap.performClick();
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

                           // sessionManager.setGPSLocations(list_dealsData.get(0).getLatitude(),list_dealsData.get(0).getLongitude(), list_dealsData.get(0).getVendorname(),list_dealsData.get(0).getAddress());


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

        llReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivReview.performClick();
            }
        });
        ivReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPulse(ivReview);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Intent intent = new Intent(context, OfferReviewActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }, DELAY_MILLIS);


            }
        });

/*        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPulse(ivShare);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            String shareDescr = "" + tvOfferTitle.getText().toString() + ".\n" +
                                    "---- Download " + getString(R.string.app_name) + " ----\n" +
                                    "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n" +
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
        });*/

        llBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CommonMethods.showDialog(spotsDialog);

                initPulse(ivBookmark);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            //Call api for insert into bookmark api

                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                            Log.d(TAG, "URL InsertBookmark " + AllKeys.WEBSITE + "InsertBookmark?type=bookmark&offerid=" + userDetails.get(SessionManager.KEY_OFFERID) + "&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "");
                            apiInterface.insertItemAsABookmark("bookmark", userDetails.get(SessionManager.KEY_OFFERID), userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<BookmarkResponse>() {
                                @Override
                                public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {


                                    Log.d(TAG, "InsertBookmark API  Called");
                                    if (response.code() == 200) {

                                        String str_error = response.body().getMESSAGE();
                                        String str_error_original = response.body().getORIGINALERROR();
                                        boolean error_status = response.body().getERRORSTATUS();
                                        boolean record_status = response.body().getRECORDS();


                                        if (error_status == false) {


                                            if(!str_error.toLowerCase().contains("already"))
                                            {

                                                ++TOTAL_BOOKMARKS;
                                                tvUnitPrice.setText(TOTAL_REVIEW + " Reviews * " + TOTAL_BOOKMARKS + " Bookmarks * " + TOTAL_USAGE + " Been There");

                                                Toast.makeText(context, "bookmark added successfully", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(context, str_error, Toast.LENGTH_SHORT).show();

                                            }




                                        } else {

                                            Toast.makeText(context, str_error, Toast.LENGTH_SHORT).show();
                                        }


                                    } else {
                                        CommonMethods.showServerError(context, response.code());

                                    }
                                    CommonMethods.hideDialog(spotsDialog);
                                }

                                @Override
                                public void onFailure(Call<BookmarkResponse> call, Throwable t) {

                                    CommonMethods.displayFailerError(context, TAG, t);
                                    CommonMethods.hideDialog(spotsDialog);
                                }
                            });


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
        Log.d(TAG, "URL ViewSingleOfferData : " + AllKeys.WEBSITE + "ViewSingleOfferData?type=offer&offerid=" + userDetails.get(SessionManager.KEY_OFFERID) + "");
        apiService.getSingleDealsDetailsFromServer("offer", userDetails.get(SessionManager.KEY_OFFERID)).enqueue(new Callback<SingleOfferData>() {
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
                        if (record_status == true) {

                            list_dealsData = response.body().getData();

                            String descr = list_dealsData.get(0).getDescription();
                            Log.d(TAG, "Descr Before : " + descr);

                            descr = descr.replace(";", "\n\n");
                            Log.d(TAG, "Descr After : " + descr);

                            sessionManager.setOfferDetails(list_dealsData.get(0).getOfferid(), list_dealsData.get(0).getTitle());
                            sessionManager.setGPSLocations(list_dealsData.get(0).getLatitude(),list_dealsData.get(0).getLongitude(), list_dealsData.get(0).getVendorname(),list_dealsData.get(0).getAddress());


                            MOBILENO = list_dealsData.get(0).getMobile();

                            //tvCommonRating.setText(" " + list_dealsData.get(0).getRating() + " ");

                            tvCommonRating.setText(String.format("%.02f", Float.parseFloat(list_dealsData.get(0).getAveragerating())));

                            // tvCommonRating.setText(" 3.5 ");

                            tvDescription.setText(descr);
                            descr = list_dealsData.get(0).getTermsandcondition();
                            descr = descr.replace(";", "\n\n");
                            tvTermsAndConditions.setText("\n" + Html.fromHtml(descr));
                            tvOfferTitle.setText(list_dealsData.get(0).getTitle());
                            tvUnitPrice.setText("\u20b9 " + list_dealsData.get(0).getUnitprice());

                            TOTAL_BOOKMARKS = Integer.parseInt(list_dealsData.get(0).getTotalbookmark());
                            TOTAL_REVIEW = Integer.parseInt(list_dealsData.get(0).getTotalratingscount());
                            TOTAL_USAGE = Integer.parseInt(list_dealsData.get(0).getTotalusage());


                            tvUnitPrice.setText(TOTAL_REVIEW + " Reviews * " + TOTAL_BOOKMARKS + " Bookmarks * " + TOTAL_USAGE + " Been There");
                            CommonMethods.hideDialog(spotsDialog);

                            //Load and item image

                            try {
                                //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

                                Picasso.with(context)
                                        .load(list_dealsData.get(0).getImg())
                                        .placeholder(R.drawable.bg3)
                                        .error(R.drawable.bg3)
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_single_offers, menu);


        this.menu = menu;
        menu_share = (MenuItem) menu.findItem(R.id.menu_share);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, DisplayDealsAcivity.class);
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.menu_share) {


            try {


                Dexter.withActivity(SingleDealDispalyActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                try {
                                    final String shareDescr = "" + tvOfferTitle.getText().toString() + ".\n" +
                                            "---- Download " + getString(R.string.app_name) + " ----\n" +
                                            "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n" +
                                            "Stay tuned and get Best Discounts.";


                              /*  Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT,
                                        shareDescr);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
*/
                                    Picasso.with(getApplicationContext()).load(list_dealsData.get(0).getImg()).into(new Target() 
									{
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            Intent i = new Intent(Intent.ACTION_SEND);
                                            i.setType("image/*");

                                            i.putExtra(Intent.EXTRA_TEXT,
                                                    shareDescr);
                                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                            startActivity(Intent.createChooser(i, "Share Image"));
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                            Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                            Toast.makeText(context, "Try again...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                                   permission, PermissionToken token) {/* ... */}
                        }).check();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return super.onOptionsItemSelected(item);
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, DisplayDealsAcivity.class);
        startActivity(intent);
        finish();
    }

}
