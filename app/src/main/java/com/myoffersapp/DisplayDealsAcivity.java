package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

    private Context context = this;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = DisplayDealsAcivity.class.getSimpleName();
    private List<DealsData.Datum> list_dealsData = new ArrayList<DealsData.Datum>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_deals_acivity);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //SetLayoutmanager to recyclerview
        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvDeals.setLayoutManager(lManager);

        //Handle Recyclerview click
        rvDeals.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvDeals, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(context,
                            SingleDealDispalyActivity.class);
                    sessionManager.setOfferDetails(list_dealsData.get(position).getOfferid());
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


        //Get All Deals/Offers Details from server
        getDealsDetailsFromServer();

    }
    //onCreate Completed


    private void getDealsDetailsFromServer() {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL " + AllKeys.WEBSITE + "ViewAllOffersData?type=offers&offertypeid=" + CommonMethods.OFFER_TYPE_GENERAL + "&branchid=" + userDetails.get(SessionManager.KEY_BRANCHIID) + "");
        apiService.getAllDealsDetailsFromServer("offers", CommonMethods.OFFER_TYPE_GENERAL, userDetails.get(SessionManager.KEY_BRANCHIID)).enqueue(new Callback<DealsData>() {
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

                        if (record_status == true)
                        {

                            list_dealsData = response.body().getData();
                            DealsAdapterRecyclerView adapter = new DealsAdapterRecyclerView(context, list_dealsData);
                            rvDeals.setAdapter(adapter);
                            CommonMethods.hideDialog(spotsDialog);

                            tvNoData.setVisibility(View.GONE);
                            rvDeals.setVisibility(View.VISIBLE);
                        }
                        else
                        {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, VendorsActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, VendorsActivity.class);
        startActivity(intent);
        finish();
    }

}
