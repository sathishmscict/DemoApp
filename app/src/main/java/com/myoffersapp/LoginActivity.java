package com.myoffersapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;

import com.myoffersapp.model.SingleUserInfo;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.myoffersapp.ui.AppButton;
import com.myoffersapp.ui.AppTextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.ivLogin)
    ImageView ivLogin;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvTerms)
    AppTextView tvTermsAndConditions;

    @BindView(R.id.btnGmail)
    AppButton btnGmail;
    private Context context = this;

    AsyncTask<Void, Void, Void> mRegisterTask;
    boolean doubleBackToExitPressedOnce = false;
    private String LOGIN_TYPE = "";

    private String PROVIDER_USERID = "0";
    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private SpotsDialog spotsDialog;
    private boolean is_signInBtn_clicked;
    private int request_code;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private GoogleSignInOptions gso;
    private String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

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

        try {
            // getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(context, AskMobileNoActivity.class);
                startActivity(intent);
                finish();*/

                LOGIN_TYPE = "gmail";

                Dexter.withActivity(LoginActivity.this)
                        .withPermission(Manifest.permission.GET_ACCOUNTS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                gPlusSignIn();

                                /*Intent intent = new Intent(context, AskMobileNoActivity.class);
                                startActivity(intent);
                                finish();
*/


                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();


            }
        });


        tvTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(AllKeys.URL_TERMS_CONDITIONS));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buidNewGoogleApiClient();


    }
//onCreate Completed


    //gmail here

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    gPlusSignIn();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


            // other 'case' lines to check for other
            // permissions this app might request

    }*/

    private void buidNewGoogleApiClient() {

        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    public void onConnectionFailed(ConnectionResult result) {
        try {
            if (!result.hasResolution()) {
                google_api_availability.getErrorDialog(this, result.getErrorCode(), request_code).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }

    @Override
    public void onConnected(Bundle arg0) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
        //  Intent in = new Intent(LoginActivity.this, AskMobileNoActivity.class);
        /*sessionmanager.createstatusKEy("0");*/
        //startActivity(in);
        // finish();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        google_api_client.connect();
    }

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected", "connected");
            is_signInBtn_clicked = true;
            //progress_dialog.show();
            CommonMethods.showDialog(spotsDialog);
            resolveSignInError();

        }
    }

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);

                //setPersonalInfo(currentPerson);


                String email = Plus.AccountApi.getAccountName(google_api_client);

                Log.d("EmailId", email);


                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                if(personPhotoUrl.contains("sz=50"))
                {
                    personPhotoUrl = personPhotoUrl.replace("sz=50","sz=250");

                }

                PROVIDER_USERID = currentPerson.getId();


                sessionManager.setUserImageUrl(personPhotoUrl);


                gPlusSignOut();
                gPlusRevokeAccess();

                sessionManager.setUserDetails(personName, email);



                sendLoginDetailsToServer(personName, email);


            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();
                CommonMethods.hideDialog(spotsDialog);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*    private void setPersonalInfo(Person currentPerson)
    {

        try {
            String personName = currentPerson.getDisplayName();
            String personPhotoUrl = currentPerson.getImage().getUrl();

            PROVIDER_USERID = currentPerson.getId();

            sessionManager.setUserImageUrl(personPhotoUrl);
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {


                }
            }

            String USER_EMAIL = Plus.AccountApi.getAccountName(google_api_client);

            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            android.accounts.Account[] accounts = AccountManager.get(context).getAccountsByType(
                    "com.google");
            for (android.accounts.Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String email12 = account.name;
                    Log.i("MY_EMAIL_count", "" + email12);
                }
            }

            //email12 = currentPerson.getId();

            Log.d("nameuser", personName);
            Log.d("emailuser", USER_EMAIL);

            sessionManager.setUserDetails(personName, USER_EMAIL);
            ///gss

          //  sendLoginDetailsToServer(personName, USER_EMAIL);
            // progress_dialog.dismiss();

            CommonMethods.hideDialog(spotsDialog);


            // sessionmanager.createsavegmaildetails(personName,personPhotoUrl,email12);
            gPlusSignOut();
            gPlusRevokeAccess();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void sendLoginDetailsToServer(String name, String email)
    {

        CommonMethods.showDialog(spotsDialog);

        //Check Login details by ApplicationInfo

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        userDetails = sessionManager.getSessionDetails();

        Log.d(TAG , "URL login  : "+AllKeys.WEBSITE+"login?type=login&mobile=&name="+ name +"&email="+ email +"&isactive=1&deviceid="+  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) +"&devicetype=android&lattitude=0.0&longitude=0.0&useravatar="+ userDetails.get(SessionManager.KEY_USER_AVATAR_URL) +"");

        apiService.sendUserLoginData("login", "", name, email, "1", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), "android", "0.0", "0.0",userDetails.get(SessionManager.KEY_USER_AVATAR_URL)).enqueue(new Callback<SingleUserInfo>() {
            @Override
            public void onResponse(Call<SingleUserInfo> call, Response<SingleUserInfo> response) {

                Log.d(TAG,"Response Code : "+response.code());

                if (response.code() == 200)
                {
                    // Toast.makeText(context, "API Called Success" + response.body().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "API Called Success" + response.toString());


                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    String userMobile = null;
                    if (error_status == false) {

                        if (record_status == true) {


                            List<SingleUserInfo.Datum> data = response.body().getData();

                            for (int i = 0; i < data.size(); i++) {
                                String name = data.get(i).getName();
                                String email = data.get(i).getEmail();
                                String userId = data.get(i).getUserid();
                                String verificationStatus = data.get(i).getVerificationstatus();
                                String gender = data.get(i).getGender();
                                String dob = data.get(i).getDob();
                                String useravatar = data.get(i).getUseravatar();

                                if(useravatar.contains("google"))
                                {
                                    useravatar  =useravatar.replace("http://discountapp.studyfield.com/","");
                                }


                                String referalcode = data.get(i).getReferalcode();
                                String devicetype = data.get(i).getDevicetype();
                                String isActive = data.get(i).getIsactive();
                                String isFirstBill = data.get(i).getIsfirstbill();
                                String isreferred = data.get(i).getReferalcode();
                                userMobile = data.get(i).getMobile();


                                sessionManager.setUserDetails(name, email, userId, verificationStatus, gender, dob, useravatar, referalcode, devicetype, isActive, isFirstBill, isreferred, userMobile);


                            }
                            CommonMethods.hideDialog(spotsDialog);


                            //disable landing screen
                            sessionManager.setFirstTimeLaunch(false);

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

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    private void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
        }
    }

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                        }

                    });
        }
    }



/*
    private void showDialog(SpotsDialog spotsDialog) {
        if(!spotsDialog.isShowing())
        {
            spotsDialog.show();

        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);


        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                //progress_dialog.dismiss();
                CommonMethods.hideDialog(spotsDialog);

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           // Log.d("GOOGLE SIGN IN", "handleSignInResult:" + result.isSuccess());
            // Signed in successfully, show authenticated UI.
           /* GoogleSignInAccount acct = result.getSignInAccount();
            UserUtil util = new UserUtil();
            SmartGoogleUser googleUser = util.populateGoogleUser(acct);*/

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

            // GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // int statusCode = result.getStatus().getStatusCode();


        }

    }

    /*private void hideDialog(SpotsDialog spotsDialog) {
        if(spotsDialog.isShowing())
        {
            spotsDialog.hide();
            spotsDialog.dismiss();



        }
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

}
