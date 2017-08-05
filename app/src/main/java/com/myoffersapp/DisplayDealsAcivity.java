package com.myoffersapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.myoffersapp.adapter.DealsAdapterRecyclerView;
import com.myoffersapp.adapter.VendorAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.VendorData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class DisplayDealsAcivity extends AppCompatActivity {

    @BindView(R.id.tvNodata)
    TextView tvNoData;

    @BindView(R.id.rvDeals)
    RecyclerView rvDeals;

    @BindView(R.id.tvKms)
    TextView tvKms;

    private Context context = this;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = DisplayDealsAcivity.class.getSimpleName();
    private List<DealsData.Datum> list_dealsData = new ArrayList<DealsData.Datum>();
    String sortingType = "normal";

    private String SORT_LATEST="latest";
    private String SORT_TRENDING="trending";
    private String SORT_RATING="rating";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deals_acivity);

        ButterKnife.bind(this);

        SlidrConfig config = new SlidrConfig.Builder().position(SlidrPosition.LEFT).scrimColor(Color.WHITE).build();
        Slidr.attach(this, config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Deals / Offers ");

        spotsDialog = new SpotsDialog(context);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        tvKms.setText("With in " + userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM) + " kms");

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SetLayoutmanager to recyclerview
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvDeals.setLayoutManager(lManager);

        //Handle Recyclerview click
       /* rvDeals.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvDeals, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(context,
                            SingleDealDispalyActivity.class);
                    sessionManager.setOfferDetails(list_dealsData.get(position).getOfferid(), list_dealsData.get(position).getTitle());
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
*/

        //Get All Deals/Offers Details from server
        getDealsDetailsFromServer();

    }
    //onCreate Completed


    private void getDealsDetailsFromServer() {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL ViewAllOffersData : " + AllKeys.WEBSITE + "ViewAllOffersData?type=offers&offertypeid=" + userDetails.get(SessionManager.KEY_OFFFER_TYPE_ID) + "&branchid=" + userDetails.get(SessionManager.KEY_BRANCHIID) + "&sortingtype="+ sortingType +"");
        apiService.getAllDealsDetailsFromServer("offers", userDetails.get(SessionManager.KEY_OFFFER_TYPE_ID), userDetails.get(SessionManager.KEY_BRANCHIID), sortingType).enqueue(new Callback<DealsData>() {
            @Override
            public void onResponse(Call<DealsData> call, retrofit2.Response<DealsData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.raw().body().toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    String userMobile = null;
                    if (error_status == false) {

                        if (record_status == true) {

                            list_dealsData = response.body().getData();
                            DealsAdapterRecyclerView adapter = new DealsAdapterRecyclerView(context, list_dealsData);
                            rvDeals.setAdapter(adapter);
                            CommonMethods.hideDialog(spotsDialog);

                            tvNoData.setVisibility(View.GONE);
                            rvDeals.setVisibility(View.VISIBLE);
                        } else {
                            tvNoData.setVisibility(View.VISIBLE);
                            tvNoData.setText("Sorry, right now no offers/deals found ");
                            rvDeals.setVisibility(View.GONE);
                        }
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<DealsData> call, Throwable t) {

                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });

    }


    //Dilaog For display Distance km dialog
    private void showKMDialog()
    {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_kilometer_range_selector);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final Button btnrange = (Button) dialog.findViewById(R.id.btnrange);

            final BubbleSeekBar bubbleSeekBar3 = (BubbleSeekBar) dialog.findViewById(R.id.demo_4_seek_bar_3);
            bubbleSeekBar3.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress, float progressFloat) {

                    btnrange.setText("FIND NEAR BY " + progress + " K.M.");


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
                    tvKms.setText("With in " + userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM) + " kms");

                    getDealsDetailsFromServer();
                }
            });

            dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //Show filter dilaog
    private void showFilterDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_offers_sorting);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button btnLatest = (Button) dialog.findViewById(R.id.btnLatest);
        final Button btnTrending = (Button) dialog.findViewById(R.id.btnTrending);
        final Button btnRating = (Button) dialog.findViewById(R.id.btnRating);

        btnLatest.setPadding(0,0,16,0);
        btnTrending.setPadding(0,0,16,0);
        btnRating.setPadding(0,0,16,0);




        if(sortingType.equals(SORT_LATEST))
        {

            btnLatest.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);
        }
        else if(sortingType.equals(SORT_TRENDING))
        {
            btnTrending.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);

        }
        else if(sortingType.equals(SORT_RATING))
        {
            btnRating.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);

        }

        btnLatest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // btnLatest.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                btnTrending.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnRating.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnLatest.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);
                //btnLatest.setPadding(0,0,16,0);


                sortingType = SORT_LATEST;
                getDealsDetailsFromServer();
                dialog.cancel();

            }
        });

        btnTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLatest.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnRating.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnTrending.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);
//                btnTrending.setPadding(0,0,16,0);


                sortingType = "trending";
                getDealsDetailsFromServer();
                dialog.cancel();



            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                btnLatest.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnTrending.setCompoundDrawablesWithIntrinsicBounds(R.color.colorPrimary, 0, 0, 0);
                btnRating.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_checked, 0);
//                btnRating.setPadding(0,0,16,0);


                sortingType = "rating";
                getDealsDetailsFromServer();
                dialog.cancel();


            }
        });


        dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_offers, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent i = null;
            try {
                Log.d(TAG, "Activity Name : " + getIntent().getStringExtra(AllKeys.ACTIVITYNAME));
                i = new Intent(context, Class.forName(context.getPackageName() + "." + getIntent().getStringExtra(AllKeys.ACTIVITYNAME)));
                i.putExtra("ActivityName", getIntent().getStringExtra("ActivityName"));
            } catch (ClassNotFoundException e) {
                i = new Intent(context, DashBoardActivity.class);
                e.printStackTrace();
            }
            startActivity(i);
            finish();

        } else if (item.getItemId() == R.id.menu_sort) {

            Log.d(TAG, "Clicked on Offers Filter");

            showFilterDialog();



        } else if (item.getItemId() == R.id.menu_location) {

            Log.d(TAG, "Clicked on Location Filter");


            //   Toast.makeText(context, "Filter", Toast.LENGTH_SHORT).show();


            //mSweetSheet3.toggle();

            showKMDialog();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = null;
        try {
            Log.d(TAG, "Activity Name : " + getIntent().getStringExtra(AllKeys.ACTIVITYNAME));
            i = new Intent(context, Class.forName(context.getPackageName() + "." + getIntent().getStringExtra(AllKeys.ACTIVITYNAME)));
            i.putExtra("ActivityName", getIntent().getStringExtra("ActivityName"));
        } catch (ClassNotFoundException e) {
            i = new Intent(context, DashBoardActivity.class);
            e.printStackTrace();
        }
        startActivity(i);
        finish();
        super.onBackPressed();

    }

}
