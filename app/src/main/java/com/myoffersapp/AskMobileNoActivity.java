package com.myoffersapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.SingleUserInfo;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskMobileNoActivity extends AppCompatActivity {

    @BindView(R.id.ivUserAvatar)
    ImageView ivProfile;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.fabNext)
    FloatingActionButton fabNext;
    @BindView(R.id.edtMobileWrapper)
    TextInputLayout edtMobileWrapper;

    @BindView(R.id.edtDob)
    EditText edtDob;

    @BindView(R.id.edtDobWrapper)
    TextInputLayout edtDobWrapper;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SpotsDialog spotsDialog;
    private String TAG = AskMobileNoActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ask_mobile_no);

        ButterKnife.bind(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setTitle(getString(R.string.title_activity_ask_mobile_no));
        } catch (Exception e) {
            e.printStackTrace();
        }



        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);



        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(edtMobile.getText().toString().length() == 10)
                {

                    edtMobileWrapper.setErrorEnabled(false);

                    sendAndUpdateLoginDetailsToServer(edtName.getText().toString(),edtEmail.getText().toString(),edtMobile.getText().toString());

                }
                else
                {

                    if(edtMobile.getText().toString().equals(""))
                    {

                        edtMobileWrapper.setErrorEnabled(true);
                        edtMobileWrapper.setError("Please enter mobile");

                    }
                    else if(edtMobile.getText().toString().length()!=10)
                    {

                        edtMobileWrapper.setErrorEnabled(true);
                        edtMobileWrapper.setError("Invalid mobile no");


                    }

                }



            }
        });


        //Set Data from gmail
        edtName.setText(userDetails.get(SessionManager.KEY_USER_NAME));
        edtMobile.setText(userDetails.get(SessionManager.KEY_USER_MOBILE));
        edtEmail.setText(userDetails.get(SessionManager.KEY_USER_EMAIL));



    }


    private void sendAndUpdateLoginDetailsToServer(String name, String email,String mobile)
    {

        CommonMethods.showDialog(spotsDialog);

        //Check Login details by ApplicationInfo

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG, "Paramteres of updateprofile : " + "updateprofile," + "," + name + "," + email + ",1," + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + ",android,0.0,0.0");

        apiService.sendAndUpdateProfileInfo("updateprofile",userDetails.get(SessionManager.KEY_USER_ID),name,email,userDetails.get(SessionManager.KEY_USER_GENDER),userDetails.get(SessionManager.KEY_USER_DOB),mobile).enqueue(new Callback<SingleUserInfo>() {
            @Override
            public void onResponse(Call<SingleUserInfo> call, Response<SingleUserInfo> response) {


                if (response.code() == 200)
                {
                    // Toast.makeText(context, "API Called Success" + response.body().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "API Called Success" + response.toString());


                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    String userMobile = null;
                    if (error_status == false)
                    {

                        if (record_status == true)
                        {


                            List<SingleUserInfo.Datum> data = response.body().getData();

                            for (int i = 0; i < data.size(); i++) {
                                String name = data.get(i).getName();
                                String email = data.get(i).getEmail();
                                String userId = data.get(i).getUserid();
                                String verificationStatus = data.get(i).getVerificationstatus();
                                String gender = data.get(i).getGender();
                                String dob = data.get(i).getDob();
                                String useravatar = data.get(i).getUseravatar();
                                String referalcode = data.get(i).getReferalcode();
                                String devicetype = data.get(i).getDevicetype();
                                String isActive = data.get(i).getIsactive();
                                String isFirstBill = data.get(i).getIsfirstbill();
                                String isreferred = data.get(i).getReferalcode();
                                userMobile = data.get(i).getMobile();


                                sessionManager.setUserDetails(name, email, userId, verificationStatus, gender, dob, useravatar, referalcode, devicetype, isActive, isFirstBill, isreferred, userMobile);


                            }
                            CommonMethods.hideDialog(spotsDialog);


                            if (userMobile.isEmpty() || userMobile == null) {

                                Intent intent = new Intent(context, AskMobileNoActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Intent intent = new Intent(context, VerificationActivity.class);
                                startActivity(intent);
                                finish();


                            }


                        }

                    } else {
                        Toast.makeText(context, str_error, Toast.LENGTH_SHORT).show();
                    }


                } else {


                    Toast.makeText(context, "Something is wrong,try again  Error code :"+response.code(), Toast.LENGTH_SHORT).show();
                }


                CommonMethods.hideDialog(spotsDialog);
            }

            @Override
            public void onFailure(Call<SingleUserInfo> call, Throwable t) {
                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, VerificationActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, VerificationActivity.class);
        startActivity(intent);
        finish();
    }
}
