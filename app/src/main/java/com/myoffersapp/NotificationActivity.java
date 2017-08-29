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
import android.widget.Button;
import android.widget.TextView;

import com.myoffersapp.adapter.NotificaitonAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.realm.model.Notification;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.rvNotificationList)
    RecyclerView rvNotificationList;

    @BindView(R.id.tvNodata)
    TextView tvNodata;
    private Realm realm;
    private RealmResults<Notification> notificationsData;
    private String TAG = NotificationActivity.class.getSimpleName();
    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setTitle("Notifications");

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
            //  getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //get realm instance
        this.realm = Realm.getDefaultInstance();

        LinearLayoutManager lManager = new LinearLayoutManager(context);
        rvNotificationList.setLayoutManager(lManager);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        FillDataOnRecylerView();

        rvNotificationList.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvNotificationList, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(context,
                            SingleDealDispalyActivity.class);
                    sessionManager.setOfferDetails(String.valueOf(notificationsData.get(position).getOfferid()), notificationsData.get(position).getTitle());
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


    }

    private void FillDataOnRecylerView() {

        realm.refresh();

        realm.beginTransaction();

        notificationsData = realm.where(Notification.class).findAll();
        //personsData.deleteAllFromRealm();
        realm.commitTransaction();

        Log.d(TAG, "Total " + notificationsData.size() + " Records Found in person master");


        if(notificationsData.size() > 0)
        {
            NotificaitonAdapterRecyclerView adapter  = new NotificaitonAdapterRecyclerView(context , notificationsData);
            rvNotificationList.setAdapter(adapter);
            rvNotificationList.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
        }
        else
        {
            rvNotificationList.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
            tvNodata.setText(getString(R.string.str_no_notifications));



        }


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
