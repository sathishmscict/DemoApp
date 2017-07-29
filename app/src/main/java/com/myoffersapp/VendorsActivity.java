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
import android.widget.Toast;

import com.myoffersapp.adapter.CategoryAdapterRecyclerView;
import com.myoffersapp.adapter.VendorAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.VendorData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class VendorsActivity extends AppCompatActivity {

    @BindView(R.id.rvVendors)
    RecyclerView rvVendors;
    private Context context = this;
    private String TAG = VendorsActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<VendorData.Datum> list_vendorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);

        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);


        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvVendors.setLayoutManager(lManager);


        //Handle Recyclerview click
        rvVendors.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvVendors, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(context,
                            DisplayDealsAcivity.class);
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


        //Get Vendots data from server
        getVendorDetailsFromServer();


    }

    private void getVendorDetailsFromServer()
    {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getAllVendorDetailsFromServer("vendor", userDetails.get(SessionManager.KEY_CATEGORYID)).enqueue(new Callback<VendorData>() {
            @Override
            public void onResponse(Call<VendorData> call, retrofit2.Response<VendorData> response) {

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

                            list_vendorData = response.body().getData();
                            VendorAdapterRecyclerView adapter = new VendorAdapterRecyclerView(context, list_vendorData);
                            rvVendors.setAdapter(adapter);
                            CommonMethods.hideDialog(spotsDialog);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, DashBoardActivity.class);
            startActivity(intent);
            finish();

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
