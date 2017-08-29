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

import com.myoffersapp.adapter.BookmarkAdapterRecyclerView;
import com.myoffersapp.adapter.EarnedOffersAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.BookmarkData;
import com.myoffersapp.model.FreeOffersData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class BookmarkActivity extends AppCompatActivity {

    @BindView(R.id.rvBookmarkList)
    RecyclerView rvBookmarkList;

    @BindView(R.id.tvNodata)
    TextView tvNodata;
    private SpotsDialog spotsDialog;
    private Context context=this;
    private String TAG = BookmarkActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spotsDialog =  new SpotsDialog(context);



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



        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvBookmarkList.setLayoutManager(lManager);





        getBookmarkDetaislFromServer();



    }
    //onCreate completed
    private void getBookmarkDetaislFromServer() {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG , "URL ViewBookMarkData  "+ AllKeys.WEBSITE +"ViewBookmarkData?type=bookmark&userid="+userDetails.get(SessionManager.KEY_USER_ID));
        /*apiService.getAllReferralsDetailsFromServer("referal", userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<ReferralData>() {*/
        apiService.getBookMarkDetailsFromServer("bookmark", userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<BookmarkData>() {

            @Override
            public void onResponse(Call<BookmarkData> call, retrofit2.Response<BookmarkData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.raw().body().toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();



                    if (error_status == false)
                    {

                        if (record_status == true) {

                            List<BookmarkData.Datum> data = response.body().getData();
                            BookmarkAdapterRecyclerView adapter = new BookmarkAdapterRecyclerView(context, data);
                            rvBookmarkList.setAdapter(adapter);


                            rvBookmarkList.setVisibility(View.VISIBLE);
                            tvNodata.setVisibility(View.GONE);

                        }
                        else
                        {
                            rvBookmarkList.setVisibility(View.GONE);
                            tvNodata.setVisibility(View.VISIBLE);
                            tvNodata.setText(getString(R.string.str_no_bookmarks_found));



                        }
                    }



                } else {

                    Toast.makeText(context, "Something is wrong,try again  Error code :"+response.code(), Toast.LENGTH_SHORT).show();
                }


                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<BookmarkData> call, Throwable t) {

                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });

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
