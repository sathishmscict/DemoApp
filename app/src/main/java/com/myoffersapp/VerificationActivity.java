package com.myoffersapp;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.NetConnectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;

public class VerificationActivity extends AppCompatActivity {

    private Context context = this;
    private EditText edt1;
    private EditText edt2, edt3, edt4;
    private FloatingActionButton fabNext;
    private TextView tvResend;
    private Timer timer;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private String TAG = VerificationActivity.class.getSimpleName();
    private SpotsDialog spotsDialog;
    private TextView tvDescr2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);


        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(AllKeys.back_button);
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvDescr2 = (TextView)findViewById(R.id.tvDescr2);
        tvDescr2.setText("via SMS for Mobile number : "+ userDetails.get(SessionManager.KEY_USER_MOBILE) +"");

        fabNext = (FloatingActionButton) findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                String currentcode = edt1.getText().toString()+edt2.getText().toString()+edt3.getText().toString()+edt4.getText().toString();

                // CheckVerification(currentcode);

                if (userDetails.get(SessionManager.KEY_CODE)
                        .equals(currentcode)) {

                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                    try {
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                // txtcode.setText(userDetails.get(SessionManager.KEY_RECEIVECODE));
                            }
                        });
                        timer.cancel();
                        timer.purge();

                        timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    VerificationStatusUpdate();





                } else {
                    Toast.makeText(context, "Invalid code", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        edt1 = (EditText) findViewById(R.id.edt1);
        edt2 = (EditText) findViewById(R.id.edt2);
        edt3 = (EditText) findViewById(R.id.edt3);
        edt4 = (EditText) findViewById(R.id.edt4);
        tvResend = (TextView) findViewById(R.id.tvResend);


        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentcode = edt1.getText().toString() + edt2.getText().toString() + edt3.getText().toString() + edt4.getText().toString();

                // CheckVerification(currentcode);

                if (userDetails.get(SessionManager.KEY_CODE)
                        .equals(currentcode)) {

                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                    try {
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                // txtcode.setText(userDetails.get(SessionManager.KEY_RECEIVECODE));
                            }
                        });
                        timer.cancel();
                        timer.purge();

                        timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    VerificationStatusUpdate();


                } else {
                    Toast.makeText(context, "Invalid code", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });


        setEditextSequesnce();

        if (NetConnectivity.isOnline(context))
        {

            timer = new Timer();
            TimerTask hourlyTask = new TimerTask() {
                @Override
                public void run() {
                    // your code here...

                    userDetails = sessionManager.getSessionDetails();

                    try {
                        userDetails.get(SessionManager.KEY_CODE);

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                            .length() == 4) {
                        if (userDetails.get(SessionManager.KEY_RECEIVECODE)
                                .equals(userDetails
                                        .get(SessionManager.KEY_CODE))) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                    /*    txtcode.setText(""
                                                + userDetails
                                                .get(SessionManager.KEY_RECEIVECODE));*/
                                        String  code  = userDetails
                                                .get(SessionManager.KEY_RECEIVECODE);
                                        edt1.setText(code.substring(0,1));
                                        edt2.setText(code.substring(1,2));
                                        edt3.setText(code.substring(2,3));
                                        edt4.setText(code.substring(3,4));
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();

                                    }
                                    // serviceP.asmx/SetStudentVerificationStatusUpdate?type=varemp&empid=string&mobileno=string&status=string&clientid=string&branch=string


                                    try {
                                        timer.cancel();
                                        timer.purge();

                                        timer = null;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            });


                        }
                    }

                }
            };

            // schedule the task to run starting now and then every hour...
            timer.schedule(hourlyTask, 0l, 1000 * 5); // 1000*10*60 every 10
            // minut
        }




        Dexter.withActivity(VerificationActivity.this)
                .withPermission(android.Manifest.permission.READ_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        sendSMSToUser();


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                           permission, PermissionToken token) {/* ... */}
                }).check();


    }
    //onCreate Completed

    private void VerificationStatusUpdate() {
        String url_update_status = AllKeys.WEBSITE + "UpdateVerificationStatus?type=status&mobile=" + userDetails.get(SessionManager.KEY_USER_MOBILE);
        Log.d(TAG, "URL UpdateVerificationStatus : " + url_update_status);
        StringRequest str_sendsms = new StringRequest(Request.Method.GET, url_update_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "Response UpdateVerificationStatus : " + response);


                sessionManager.CheckSMSVerificationActivity("",
                        "1");


                if (userDetails.get(SessionManager.KEY_USER_IS_FIRST_BILL).equals("0") || userDetails.get(SessionManager.KEY_USER_IS_REFERRED).equals("1")) {
                    Intent intent = new Intent(context, SecondaryDashboard.class);
                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(context, DashBoardActivity.class);
                    startActivity(intent);
                    finish();

                }


                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof ServerError || error instanceof NetworkError) {
                    hideDialog();
                } else {
                    VerificationStatusUpdate();
                }


                Log.d(TAG, "Error Response :" + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_sendsms);

    }


    private void setEditextSequesnce() {
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    edt4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 1) {
                    fabNext.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }


    public void sendSMSToUser() {


        String sendsms = "";

        if (userDetails.get(SessionManager.KEY_CODE).equals("0")) {
            Random r = new Random();
            int code = r.nextInt(9999 - 1000) + 1000;
            Log.d(TAG, "Verification Code : " + code);
            // sendsms = AllKeys.WEBSITE + "sendSMS/" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "/" + code + "/otp";

            sendsms = AllKeys.WEBSITE + "VerificationService?type=verification&code=" + code + "&mobile=" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "";


            Log.d(TAG, "URL sendSMS : " + sendsms);

            sessionManager.createUserSendSmsUrl("" + code, sendsms);
        } else {

            userDetails = sessionManager.getSessionDetails();
            //sendsms = AllKeys.WEBSITE + "sendSMS/" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "/" + userDetails.get(SessionManager.KEY_CODE) + "/otp";

            sendsms = AllKeys.WEBSITE + "VerificationService?type=verification&code=" + userDetails.get(SessionManager.KEY_CODE) + "&mobile=" + userDetails.get(SessionManager.KEY_USER_MOBILE) + "";


            //sendsms = userDetails.get(SessionManager.KEY_SMSURL);
            Log.d(TAG, "URL sendSMS : " + sendsms);


        }

        Log.d(TAG, "sendsms res : " + sendsms);

        showDialog();


        StringRequest str_sendsms = new StringRequest(Request.Method.GET, sendsms, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Send SMS Response :" + response);


                try {
                    JSONObject obj = new JSONObject(response);


                    if (obj.getBoolean(AllKeys.TAG_ERROR_STATUS) == false) {

                        Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Error in message sending,try again...", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();


                hideDialog();


                Log.d(TAG, "Error Response :" + error.getMessage());
            }
        });
        MyApplication.getInstance().addToRequestQueue(str_sendsms);


    }

    private void showDialog() {
        if (!spotsDialog.isShowing()) {
            spotsDialog.show();

        }
    }

    private void hideDialog() {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();


        }
    }


}
