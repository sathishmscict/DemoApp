package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myoffersapp.adapter.DealsAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.SingleOfferData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
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



    private Context context=this;
    private SpotsDialog spotsDialog;
    private String TAG = SingleDealDispalyActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private List<SingleOfferData.Datum> list_dealsData= new ArrayList<SingleOfferData.Datum>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_deal_dispaly);

        ButterKnife.bind(this);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnUseThisOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });


        getSingleOfferDetailsFromServer();









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



                            tvDescription.setText(descr);
                            descr = list_dealsData.get(0).getTermsandcondition();
                            descr = descr.replace(";", "\n\n");
                            tvTermsAndConditions.setText("\n"+Html.fromHtml(descr+descr+descr+descr+descr));
                            tvOfferTitle.setText(list_dealsData.get(0).getTitle());
                            tvUnitPrice.setText("\u20b9 "+list_dealsData.get(0).getUnitprice());
                            CommonMethods.hideDialog(spotsDialog);

                            //Load and item image

                            try {
                                //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

                                Picasso.with(context)
                                        .load(list_dealsData.get(0).getImg())
                    .placeholder(R.drawable.bg2)
                    .error(R.drawable.bg2)
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
