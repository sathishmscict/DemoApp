package com.myoffersapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myoffersapp.adapter.DealsAdapterRecyclerView;
import com.myoffersapp.adapter.ReviewAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.ReviewData;
import com.myoffersapp.model.ReviewReponse;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferReviewActivity extends AppCompatActivity {

    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    @BindView(R.id.tvReviewsRating)
    TextView tvReviewsRating;

    @BindView(R.id.tvBadesOn)
    TextView tvBadesOn;


    private Context context = this;
    private SpotsDialog spotsDialog;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = OfferReviewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_review);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("All Reviews");

        spotsDialog = new SpotsDialog(context);
        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showAddReviewDialog();
            }
        });


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);

        } catch (Exception e) {
            e.printStackTrace();
        }


        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvReviews.setLayoutManager(lManager);



        getAllReviewDetailsByOfferid();


    }

    private void getAllReviewDetailsByOfferid()
    {

        //Here code for get all review details by offerid
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG, "URL ViewReviewData  " + AllKeys.WEBSITE + "ViewReviewData?type=review&offerid=" + userDetails.get(SessionManager.KEY_OFFERID) + "");
        apiInterface.getAllReviewDetailsByOfferId("review", userDetails.get(SessionManager.KEY_OFFERID)).enqueue(new Callback<ReviewData>() {
        /*apiInterface.getAllReviewDetailsByOfferId("review", "1").enqueue(new Callback<ReviewData>() {*/
            @Override
            public void onResponse(Call<ReviewData> call, Response<ReviewData> response) {

                if (response.code() == 200) {
                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false) {
                        if (record_status == true) {

                            tvReviewsRating.setText(String.format("%.02f",Float.parseFloat(response.body().getAVERAGERATE())));
                            tvBadesOn.setText("Based on " + response.body().getTOTALREVIEW() + " Reviews");

                            List<ReviewData.Datum> list_reviewData = response.body().getData();
                            ReviewAdapterRecyclerView adapter = new ReviewAdapterRecyclerView(context, list_reviewData);
                            rvReviews.setAdapter(adapter);


                        } else {
                            tvReviewsRating.setText(response.body().getAVERAGERATE());
                            tvBadesOn.setText("Based on " + response.body().getTOTALREVIEW() + " Reviews");


                        }
                        CommonMethods.hideDialog(spotsDialog);

                    } else {

                        CommonMethods.showServerError(context, response.code());
                        CommonMethods.hideDialog(spotsDialog);


                    }

                }
            }

            @Override
            public void onFailure(Call<ReviewData> call, Throwable t) {
                CommonMethods.displayFailerError(context, TAG, t);

                CommonMethods.hideDialog(spotsDialog);
            }
        });

    }


    private void showAddReviewDialog() {
        try {


            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_review_offer);


            TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.cancel();
                    dialog.dismiss();
                }
            });
            final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
            final EditText edtReview = (EditText) dialog.findViewById(R.id.edtReview);
            TextInputLayout edtReviewWrapper = (TextInputLayout) dialog.findViewById(R.id.edtReviewWrapper);
            Button btnSubmitReview = (Button) dialog.findViewById(R.id.btnSubmitReview);

            btnSubmitReview.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    if (ratingBar.getRating() == 0.0) {



                        Toast.makeText(OfferReviewActivity.this, "Please give rating...", Toast.LENGTH_SHORT).show();

                    /*
                        CommonMethods.showDialog(spotsDialog);
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                        apiInterface.insertReviewDetailsFromServer("review", userDetails.get(SessionManager.KEY_USER_ID), edtReview.getText().toString(), String.valueOf(ratingBar.getRating()), userDetails.get(SessionManager.KEY_OFFERID)).enqueue(new Callback<ReviewReponse>() {
                            @Override
                            public void onResponse(Call<ReviewReponse> call, Response<ReviewReponse> response) {

                                Log.d(TAG , "respone Code : "+response.code());
                                if (response.code() == 200) {

                                    dialog.cancel();
                                    dialog.dismiss();

                                    Toast.makeText(context, "Review has been submitted", Toast.LENGTH_SHORT).show();


                                } else {
                                    CommonMethods.showServerError(context, response.code());


                                }
                                CommonMethods.hideDialog(spotsDialog);
                            }

                            @Override
                            public void onFailure(Call<ReviewReponse> call, Throwable t) {

                                CommonMethods.displayFailerError(context, TAG, t);

                                CommonMethods.hideDialog(spotsDialog);
                            }
                        });*/


                    } else {

                        sendReviewDetailsToServer(edtReview.getText().toString(), String.valueOf(ratingBar.getRating()), dialog);


                    }


                }
            });


            dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.show();


            //Programatically hide Keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) OfferReviewActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = OfferReviewActivity.this.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(OfferReviewActivity.this);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendReviewDetailsToServer(String descr, String rating, final Dialog dialog) {

        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL : " + AllKeys.WEBSITE + "InsertReview?type=review&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&description=" + descr + "&rate=" + rating + "&offerid=" + userDetails.get(SessionManager.KEY_OFFERID) + "");
        apiService.insertReviewDetailsFromServer("review", userDetails.get(SessionManager.KEY_USER_ID), descr, rating, userDetails.get(SessionManager.KEY_OFFERID)).enqueue(new Callback<ReviewReponse>() {
            @Override
            public void onResponse(Call<ReviewReponse> call, retrofit2.Response<ReviewReponse> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false) {

                        /*if (record_status == true) {

                        }*/

                        getAllReviewDetailsByOfferid();
                        CommonMethods.hideDialog(spotsDialog);
                        dialog.cancel();
                        dialog.dismiss();
                        Toast.makeText(context, "Thank you,review has been submitted", Toast.LENGTH_SHORT).show();
                    } else {
                        CommonMethods.hideDialog(spotsDialog);
                        Toast.makeText(context, "Sorry , try again...", Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "Orginal Error : " + str_error_original);
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<ReviewReponse> call, Throwable t) {


                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
                Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
                Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
                Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());

                Toast.makeText(context, "Sorry , try again...", Toast.LENGTH_SHORT).show();

                if (t.getMessage().equals("timeout")) {
                    // sendReviewDetailsToServer();
                }

                CommonMethods.hideDialog(spotsDialog);
            }
        });
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
