package com.myoffersapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.myoffersapp.app.MyApplication;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.ImageUtils;
import com.myoffersapp.helper.NetConnectivity;
import com.myoffersapp.helper.Utility;
import com.myoffersapp.model.SingleUserInfo;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class FragmentProfile extends Fragment {


    private Context context = getActivity();
    private String gender;

    private String name;
    private String email;
    private String dob;
    private String pincode;


    private TextView txtname;
    private TextView txtmobile;
    private TextView txtemail;

    ArrayAdapter<String> occuptionadapter;


    private static EditText edtdob;







    private TextView txtnext;
    private String query;
    private Cursor c;

    private String deviceid;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private static int y;
    private static int m;
    private static int d;
    private int spm;
    private static String startDate;
    private String showsdate;
    static final int DATE_PICKER_ID = 1111;
    private int uid;
    private String vresval;
    private int ID = 0;



    private String COUNTRYFLAG = "";
    private String STATEFLAG = "";

    //private TextView txtlogout;
    //private TextView txtupdate;



    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    private TextView txttitle;




    private static boolean DATE_DOB = false;
    private ImageView imgProfilePic;
    private String userChoosenTask;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Bitmap bitmap;
    private MenuItem profile_edit, profile_update;
    private LinearLayout llname, llemail;
    private EditText txtname_edit;
    private EditText txtemail_edit;
    private String TAG = FragmentProfile.class.getSimpleName();


    private SpotsDialog spotsDialog;

    private LinearLayout llDOB;

    private RadioGroup rdGrpGender;
    private String GENDER_ID;
    private TextInputLayout edtDobWrapper;
    private TextInputLayout txtname_editWrapper;
    private TextInputLayout txtemail_editWrapper;
    private RadioButton rdMale, rdFemale;
    private LinearLayout mRoot;





    private String BASE64STRING="";


    //private boolean IsUpdatePassword = false;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        sessionManager = new SessionManager(getActivity());
        userDetails = new HashMap<String, String>();

        userDetails = sessionManager.getSessionDetails();





        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.setCancelable(false);

        //Getting IMEI No. from  current device

        //TelephonyManager mngr = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        //deviceid = mngr.getDeviceId();

        mRoot = (LinearLayout) rootView.findViewById(R.id.llmain);

        txtname = (TextView) rootView.findViewById(R.id.txtname);
        txtemail = (TextView) rootView.findViewById(R.id.txtemail);


        llname = (LinearLayout) rootView.findViewById(R.id.llname);
        llemail = (LinearLayout) rootView.findViewById(R.id.llemail);
        llDOB = (LinearLayout) rootView.findViewById(R.id.llDOB);

        rdGrpGender = (RadioGroup) rootView.findViewById(R.id.rdGrpGender);
        rdMale = (RadioButton) rootView.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) rootView.findViewById(R.id.rdFemale);











        //  cvUpdatePasswprd.setVisibility(View.GONE);




        if (userDetails.get(SessionManager.KEY_USER_GENDER).toLowerCase().equals("female")) {


            rdFemale.setChecked(true);
            GENDER_ID = "Female";
        } else {
            GENDER_ID = "Male";
            rdMale.setChecked(true);
        }


        rdGrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.rdMale) {
                    GENDER_ID = "Male";

                } else if (checkedId == R.id.rdFemale) {

                    GENDER_ID = "Female";
                }
            }
        });





        txtname_edit = (EditText) rootView.findViewById(R.id.txtname_edit);
        txtemail_edit = (EditText) rootView.findViewById(R.id.txtemail_edit);


        txtmobile = (TextView) rootView.findViewById(R.id.txtmobile);


        txtname_edit.setText( userDetails.get(SessionManager.KEY_USER_NAME));
        txtemail_edit.setText( userDetails.get(SessionManager.KEY_USER_EMAIL));



        //RdMarital = (RadioGroup)rootView.findViewById(R.id.radioGroup2);
        edtdob = (EditText) rootView.findViewById(R.id.edtdob);
        edtDobWrapper = (TextInputLayout) rootView.findViewById(R.id.edtDobWrapper);



        txtname_editWrapper = (TextInputLayout) rootView.findViewById(R.id.txtname_editWrapper);
        txtemail_editWrapper = (TextInputLayout) rootView.findViewById(R.id.txtemail_editWrapper);








        /*txtcoursestartdate.setText("" + userdetails.get(SessionManager.KEY_COURSE_START_DATE));
        txtcourseenddate.setText("" + userdetails.get(SessionManager.KEY_COURSE_END_DATE));*/


        if (!userDetails.get(SessionManager.KEY_USER_DOB).equals("null")) {
            edtdob.setText("" + userDetails.get(SessionManager.KEY_USER_DOB));
        }

        //txtdoa.setText("" + userdetails.get(SessionManager.KEY_US));




        txttitle = (TextView) rootView.findViewById(R.id.textView3);

        imgProfilePic = (ImageView) rootView.findViewById(R.id.imgProfilePic);

        HideControls();

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }


        SetUserProfilePictireFromBase64EnodedString();



        //txtpincode = (EditText)rootView.findViewById(R.id.txtpostalcode);

        txtname.setText("" + userDetails.get(SessionManager.KEY_USER_NAME));
        txtmobile.setText("+91 " + userDetails.get(SessionManager.KEY_USER_MOBILE));
        txtemail.setText("" + userDetails.get(SessionManager.KEY_USER_EMAIL));
        /*txtphoneno.setText("" + userdetails.get(SessionManager.KEY_PHONENO));*/


        //	txtpincode.setText("" + PINCODE);

        //deviceid = Settings.Secure.getString(context.getContentResolver(),
        //Settings.Secure.ANDROID_ID);

        // txtnext = (TextView)findViewById(R.id.txtnext);

        final Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);

        spm = m + 1;
        if (spm <= 9) {
            String mm = "0" + spm;
            startDate = y + "-" + mm + "-" + d;
            showsdate = y + "-" + mm + "-" + d;
            if (y == cal.get(Calendar.YEAR)) {
                y = 1995;
            }
        } else {
            startDate = y + "-" + spm + "-" + d;
            showsdate = y + "-" + "d" + "-" + spm;
            if (y == cal.get(Calendar.YEAR)) {
                y = 1995;
            }


        }

        edtdob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //your code
                        DATE_DOB = true;
                        //  getActivity().showDialog(DATE_PICKER_ID);

                        DialogFragment picker = new DatePickerFragment();
                        picker.show(getFragmentManager(), "datePicker");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return false;
            }
        });







        /**
         * Set Profile Picture
         */
        imgProfilePic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {




                Dexter.withActivity(getActivity())
                        .withPermissions(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE

                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        selectImage();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }).check();



            }
        });




        // Inflate the layout for this fragment
        return rootView;
    }


    private void HideControls() {

        edtdob.setEnabled(false);

        txtname_edit.setEnabled(false);
        txtemail_edit.setEnabled(false);








        //llname.setVisibility(View.GONE);
        //llemail.setVisibility(View.GONE);

       // llDOB.setVisibility(View.GONE);
        //rdGrpGender.setVisibility(View.GONE);



    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)

            result += line;

        inputStream.close();
        return result;
    }




    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        private String showdate = "";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            y = year;
            m = month;
            d = day;

            // Show selected date

            String dd = "";
            if (d <= 9) {
                dd = "0" + d;
            } else {
                dd = "" + d;

            }


            //   String startDate;
            int spm = m + 1;
            if (spm <= 9) {
                String mm = "0" + spm;
                startDate = dd + "-" + mm + "-" + y;
                //startDate = AllKeys.convertToJsonDateFormat(startDate);
                showdate = dd + "-" + mm + "-" + y;
            } else {
                startDate = y + "-" + spm + "-" + dd;
                //startDate = AllKeys.convertToJsonDateFormat(startDate);
                showdate = dd + "-" + spm + "-" + y;
            }
            if (DATE_DOB == true) {

                edtdob.setText(showdate);
            }

        }
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        //inflater.inflate(R.menu.menu_profile, menu);


        super.onCreateOptionsMenu(menu, inflater);


        try {
            getActivity().getMenuInflater().inflate(R.menu.menu_profile, menu);


            profile_edit = (MenuItem) menu.findItem(R.id.action_edit);

            profile_update = (MenuItem) menu.findItem(R.id.action_update);

            profile_update.setVisible(false);


            if (userDetails.get(SessionManager.KEY_USER_NAME).equals("")) {
                profile_update.setVisible(true);
                profile_edit.setVisible(false);

                profile_update.setVisible(true);
                profile_edit.setVisible(false);
                ShowAllControlsEditableAndVisible();

            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement


        if (id == R.id.action_edit) {
            {


                //  IsUpdatePassword = false;
                profile_update.setVisible(true);
                profile_edit.setVisible(false);



                ShowAllControlsEditableAndVisible();

            }

        }

        if (id == R.id.action_update) {

            if (NetConnectivity.isOnline(getActivity())) {
                try {


                  /*  if (IsUpdatePassword == false)
                    {
*/
                    Boolean isError = false;

                    if (edtdob.getText().toString().equals("")) {

                        isError = true;
                        edtDobWrapper.setErrorEnabled(true);
                        edtDobWrapper.setError("Enter DateOfBirth");
                    } else {
                        edtDobWrapper.setErrorEnabled(false);

                    }

                    if (txtname_edit.getText().toString().equals("")) {
                        isError = true;
                        txtname_editWrapper.setErrorEnabled(true);
                        txtname_editWrapper.setError("Enter Name");
                    } else {

                        txtname_editWrapper.setErrorEnabled(false);
                    }


                    if (txtemail_edit.getText().toString().equals("")) {

                        isError = true;
                        txtemail_editWrapper.setErrorEnabled(true);
                        txtemail_editWrapper.setError("Please Enter Email");

                    } else {
                        txtemail_editWrapper.setErrorEnabled(false);

                        if (CommonMethods.checkEmail(txtemail_edit.getText().toString())) {
                            txtemail_editWrapper.setErrorEnabled(false);
                        } else {
                            isError = true;
                            txtemail_editWrapper.setErrorEnabled(true);
                            txtemail_editWrapper.setError("Invalid Email");

                        }
                    }


                    //new SendProfileDetailsToServer().execute();


                    if (isError == false) {
                        //sendProfileUpdateDetialsToServer();
                        sendAndUpdateLoginDetailsToServer(txtname_edit.getText().toString() , txtemail_edit.getText().toString() , userDetails.get(SessionManager.KEY_USER_MOBILE));
                    }




                } catch (Exception e) {
                    System.out.print("Errorr :" + e.getMessage());
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(getActivity(), "Please enable wifi or mobile data", Toast.LENGTH_SHORT).show();


            }


        }

        return super.onOptionsItemSelected(item);
    }

    private void sendProfileUpdateDetialsToServer() {




        /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = originalFormat.parse("August 21, 2012");
        String formattedDate = targetFormat.format(date);  // 20120821*/

        String formattedDate;

        try {
            DateFormat originalFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(edtdob.getText().toString());
            formattedDate = targetFormat.format(date);  // 20120821

        } catch (ParseException e) {
            formattedDate = "";  // 20120821
            e.printStackTrace();
        }


        String url_profileUpdate = AllKeys.WEBSITE + "updateUserDetails/" + userDetails.get(SessionManager.KEY_USER_ID) + "";
        http:
//192.168.0.21/yelona/index.php/welcome/updateUserDetails/775/Gadde/sathishmicit2012%40gmail.com/11-03-1993/1/
        Log.d(TAG, "URL Profile Update  :" + url_profileUpdate);

        StringRequest str_profileUpdate = new StringRequest(Request.Method.GET, url_profileUpdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response Update Profile :" + response);

                if (response.contains("success")) {

                    //sessionManager.setUserDetails(userdetails.get(SessionManager.KEY_USER_ID), txtname_edit.getText().toString(), userdetails.get(SessionManager.KEY_USER_LASTNAME), userdetails.get(SessionManager.KEY_USER_AVATAR), userdetails.get(SessionManager.KEY_USER_NAME), txtemail_edit.getText().toString(), userdetails.get(SessionManager.KEY_USER_PASSWORD), userdetails.get(SessionManager.KEY_USER_MOBILE), edtdob.getText().toString(), GENDER_ID, edtBio.getText().toString(), userdetails.get(SessionManager.KEY_USER_LASTLOGIN), userdetails.get(SessionManager.KEY_USER_VERIFIED_MOBILE), userdetails.get(SessionManager.KEY_USER_VERIFIED_EMAIL), "");


                    profile_edit.setVisible(true);
                    profile_update.setVisible(false);
                    txtname.setText(txtname_edit.getText().toString());
                    txtemail.setText(txtemail_edit.getText().toString());

                    HideControls();

                    Snackbar.make(mRoot, "Profile details updated", Snackbar.LENGTH_LONG).show();



                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Profile Info ");
                    alertDialogBuilder.setMessage("Profile details has been updated successfully");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {

                                    dialog.cancel();
                                    dialog.dismiss();
                                    //txtStatus.setVisibility(View.GONE);
                                   // btnUpdate.setVisibility(View.GONE);

                                }
                            });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                } else {
                    Snackbar.make(mRoot, "Sorry, try again...", Snackbar.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError || error instanceof NetworkError) {

                    CommonMethods.hideDialog(spotsDialog);
                } else {
                    sendProfileUpdateDetialsToServer();
                }

            }
        });
        MyApplication.getInstance().addToRequestQueue(str_profileUpdate);



    }

    // update using Retrofit library
    private void sendAndUpdateLoginDetailsToServer(String name, String email,String mobile)
    {

        CommonMethods.showDialog(spotsDialog);

        //Check Login details by ApplicationInfo

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //Log.d(TAG, "Paramteres of updateprofile : " + "type=updateprofile&gender="+ GENDER_ID  +"&mobile="+ mobile +"&dob="+ CommonMethods.convertToJsonDateFormat(edtdob.getText().toString()) +"&userid=" +userDetails.get(SessionManager.KEY_USER_ID)+ "&name=" + name + "&email=" + email + "&deviceid=" + Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID) + "&Devicetpe=android&mobile="+ txtmobile.getText().toString() +",0.0,0.0");

        Log.d(TAG, "URL UpdateProfile  : " + AllKeys.WEBSITE+"UpdateProfile?type=updateprofile&userid="+ userDetails.get(SessionManager.KEY_USER_ID) +"&name=" + name + "&email=" + email + "gender="+ GENDER_ID +"&dob="+ CommonMethods.convertToJsonDateFormat(edtdob.getText().toString()) +"&mobile="+ userDetails.get(SessionManager.KEY_USER_MOBILE) +"");



        apiService.sendAndUpdateProfileInfo("updateprofile",userDetails.get(SessionManager.KEY_USER_ID),name,email,userDetails.get(SessionManager.KEY_USER_GENDER),CommonMethods.convertToJsonDateFormat(edtdob.getText().toString()),mobile).enqueue(new Callback<SingleUserInfo>() {
            @Override
            public void onResponse(Call<SingleUserInfo> call, retrofit2.Response<SingleUserInfo> response) {


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

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("Profile Info ");
                            alertDialogBuilder.setMessage("Profile details has been updated successfully");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int arg1) {

                                            dialog.cancel();
                                            dialog.dismiss();
                                            //txtStatus.setVisibility(View.GONE);
                                            // btnUpdate.setVisibility(View.GONE);

                                        }
                                    });


                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();




                        }

                    } else {
                        Toast.makeText(getActivity(), str_error, Toast.LENGTH_SHORT).show();
                    }


                } else {


                    Toast.makeText(getActivity(), "Something is wrong,try again  Error code :"+response.code(), Toast.LENGTH_SHORT).show();
                }


                CommonMethods.hideDialog(spotsDialog);
            }

            @Override
            public void onFailure(Call<SingleUserInfo> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });





    }




    private void ShowAllControlsEditableAndVisible() {

        edtdob.setEnabled(true);




        txtemail_edit.setEnabled(true);
        txtname_edit.setEnabled(true);

        llemail.setVisibility(View.VISIBLE);
        llname.setVisibility(View.VISIBLE);

        llDOB.setVisibility(View.VISIBLE);
        rdGrpGender.setVisibility(View.VISIBLE);

        txtname_edit.setVisibility(View.VISIBLE);

        txtname_edit.setText("" + txtname.getText().toString());
        //txtemail.setVisibility(View.VISIBLE);
        txtemail_edit.setText("" + txtemail.getText().toString());


    }






    public class SendProfileDetailsToServer extends AsyncTask<Void, Void, Void> {

        //private Dialog dialog;
        private ProgressBar progressBar;
        private String response_signup = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  dialog = new Dialog(getActivity());

            //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // dialog.setContentView(R.layout.dialog_progress);


            // progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar2);
            // progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);

         /*   Display display = getActivity().getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();
            height = height / 3;// - 100;
            width = width - 20;

            dialog.getWindow().setLayout(width, height);
            dialog.setCancelable(false);
            dialog.show();*/

            CommonMethods.showDialog(spotsDialog);


        }

        @Override
        protected Void doInBackground(Void... paramssss) {


           /* ServiceHandler sh = new ServiceHandler();


            //String URL_CREATE_NEW_USER = AllKeys.TAG_WEBSITE+"/GetClientRegitration?type=signin&name="+ txtfullname.getText().toString() +"&username="+ txtusername.getText().toString() +"&password="+ txtpassword.getText().toString() +"&mobile="+ txtmobile.getText().toString() +"&email="+ txtemail.getText().toString() +"&deviceid="+ DEVICEID+"sa" +"&phno="+ txtphoneno.getText().toString() +"&ver_code="+ code +"";
            String URL_CREATE_NEW_USER = AllKeys.WEBSITE + "UpdateCustomerDetail";


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("type", "updatecustomer"));
            params.add(new BasicNameValuePair("custid", userdetails.get(SessionManager.KEY_USERID)));
            params.add(new BasicNameValuePair("name", dbhandler.convertEncodedString(txtname_edit.getText().toString())));
            params.add(new BasicNameValuePair("phoneno", dbhandler.convertEncodedString(txtphoneno.getText().toString())));
            params.add(new BasicNameValuePair("address", dbhandler.convertEncodedString(txtaddress.getText().toString())));
            params.add(new BasicNameValuePair("city", dbhandler.convertEncodedString(txtcity.getText().toString())));
            params.add(new BasicNameValuePair("state", dbhandler.convertEncodedString(txtstate.getText().toString())));
            params.add(new BasicNameValuePair("mobile", dbhandler.convertEncodedString(userdetails.get(SessionManager.KEY_MOBILE))));
            params.add(new BasicNameValuePair("email", dbhandler.convertEncodedString(txtemail_edit.getText().toString())));


            if(txtdob.getText().toString().equals(""))
            {
                params.add(new BasicNameValuePair("dob", ""));

            }
            else
            {
            params.add(new BasicNameValuePair("dob", dbhandler.convertToJsonDateFormat(txtdob.getText().toString())));

            }




            if(txtdoa.getText().toString().equals(""))
            {
            params.add(new BasicNameValuePair("doa", ""));

            }
            else
            {

                params.add(new BasicNameValuePair("doa", dbhandler.convertToJsonDateFormat(txtdoa.getText().toString())));
            }
            params.add(new BasicNameValuePair("OccupationId", String.valueOf(listOccupationId.get(spnoccupation.getSelectedItemPosition()))));


            session.setUserDetails(txtname_edit.getText().toString(), userdetails.get(SessionManager.KEY_MOBILE), txtemail_edit.getText().toString(), txtcity.getText().toString(), txtstate.getText().toString(), userdetails.get(SessionManager.KEY_USERID), txtphoneno.getText().toString(), txtaddress.getText().toString(), userdetails.get(SessionManager.KEY_PROGRAMMEID), txtprogrammename.getText().toString(), txtcoursestartdate.getText().toString(), txtcourseenddate.getText().toString(), txtdoa.getText().toString(), txtdob.getText().toString(), "" + listOccupationId.get(spnoccupation.getSelectedItemPosition()));

            Log.d("URL GetClientReg :", URL_CREATE_NEW_USER + params.toString());


            response_signup = sh.makeServiceCall(URL_CREATE_NEW_USER, ServiceHandler.POST, params);

            Log.d("Response  ", "ProfileUpdate : " + response_signup);*/

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

CommonMethods.hideDialog(spotsDialog);

       /*     if (response_signup.equals("1")) {
                //Toast.makeText(getActivity() , "Details has been update",Toast.LENGTH_SHORT).show();

                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Profile details has been update successfully");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getActivity(),
                                NewDashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("PROFILEUPDATE","TRUE");
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);




                    }
                });


                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            } else
                {

                //Toast.makeText(getActivity() , "Details not updated",Toast.LENGTH_SHORT).show();

                final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Profile details not update successfully");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        *//*Intent intent = new Intent(getActivity(),
                                NewDashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.slide_left, R.anim.slide_right);*//*


                    }
                });


                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }*/


        }
    }


    /**
     * User Profile Pic Selection Related Code
     */


    private void SetUserProfilePictireFromBase64EnodedString() {
        try {


            String myBase64Image = userDetails.get(SessionManager.KEY_ENODEDED_STRING);//Here get image and convert into base64
            if (!myBase64Image.equals("")) {

                Bitmap myBitmapAgain = CommonMethods.decodeBase64(myBase64Image);

                imgProfilePic.setImageBitmap(myBitmapAgain);


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Decode Img Exception : ", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Profile Picture!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                boolean result = Utility.checkPermission_ExternalStorage(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    private void onCaptureImageResult(Intent data) {

        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);


        int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
        Log.d("C:Before Bitmap Size : ", "" + bitmapByteCount);


        //String realPath=getRealPathFromURI(data.getData());

        Uri tempUri = getImageUri(bitmap);


        String realPath = null;
        try {
            Log.d("C: Realpath URI : ", "" + tempUri.toString());
            realPath = getRealPathFromURI(tempUri);
            Log.d("C: Realpath : ", realPath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        bitmap = ImageUtils.getInstant().getCompressedBitmap(realPath);
        //imageView.setImageBitmap(bitmap);

        bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
        Log.d("C:After Bitmap Size : ", "" + bitmapByteCount);


        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


     //   imgProfilePic.setImageBitmap(bitmap);


        if (NetConnectivity.isOnline(getActivity())) {

            BASE64STRING= CommonMethods.getStringImage(bitmap);
            sessionManager.setEncodedImage(BASE64STRING);

            SendProfilePictureToServer();

            //  new SendUserProfilePictureToServer().execute();
        } else {

            //   checkInternet();
            Toast.makeText(getActivity(), "Please enable internet", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {


                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                int bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("Before Bitmap Size : ", "" + bitmapByteCount);


                Uri tempUri = getImageUri(bitmap);


                String realPath = null;
                try {
                    Log.d("CC: Realpath URI : ", "" + tempUri.toString());
                    realPath = getRealPathFromURI(tempUri);
                    Log.d("CC: Realpath : ", realPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*Uri uri = data.getData();

                realPath=""+getRealPathFromURI(data.getData());
                Log.d("RealPath : " , ""+realPath);
                realPath = uri.getEncodedPath();
                Log.d("RealPath URI : " , ""+realPath);
                realPath=""+getRealPathFromURI_NEW(data.getData());
                Log.d("RealPath New : " , ""+realPath);*/


                bitmap = ImageUtils.getInstant().getCompressedBitmap(realPath);
                //imageView.setImageBitmap(bitmap);

                bitmapByteCount = BitmapCompat.getAllocationByteCount(bitmap);
                Log.d("After Bitmap Size : ", "" + bitmapByteCount);


                // getStringImage(bm);


            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        try {
            if (NetConnectivity.isOnline(getActivity())) {


                BASE64STRING = CommonMethods.getStringImage(bitmap);
               // sessionManager.setEncodedImage(BASE64STRING);
                //imgProfilePic.setImageBitmap(bitmap);

                SendProfilePictureToServer();
            } else {
//                checkInternet();
                Toast.makeText(getActivity(), "Please enable internet", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




        private void SendProfilePictureToServer()
        {




            CommonMethods.showDialog(spotsDialog);

            //Check Login details by ApplicationInfo

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Log.d(TAG, "URL sendUserAvaterToServer : " +AllKeys.WEBSITE +"type=updateavatar&userid="+ userDetails.get(SessionManager.KEY_USER_ID) +"*imagecode="+ BASE64STRING +"");




            apiService.sendUserAvaterToServer("updateavatar",userDetails.get(SessionManager.KEY_USER_ID),BASE64STRING).enqueue(new Callback<SingleUserInfo>() {
                @Override
                public void onResponse(Call<SingleUserInfo> call, retrofit2.Response<SingleUserInfo> response) {


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

                                    sessionManager.setUserImageUrl(data.get(i).getUseravatar());
                                    sessionManager.setEncodedImage(BASE64STRING);
                                    imgProfilePic.setImageBitmap(bitmap);
                                }

                                CommonMethods.hideDialog(spotsDialog);



                            }

                        } else {
                            Toast.makeText(getActivity(), str_error, Toast.LENGTH_SHORT).show();
                        }


                    } else {


                        Toast.makeText(getActivity(), "Something is wrong,try again  Error code :"+response.code(), Toast.LENGTH_SHORT).show();
                    }


                    CommonMethods.hideDialog(spotsDialog);
                }

                @Override
                public void onFailure(Call<SingleUserInfo> call, Throwable t) {
                    Toast.makeText(getActivity(), "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                    CommonMethods.hideDialog(spotsDialog);
                }
            });




        }


    /**
     * Complete User select profile picture from device
     */


    /*public String getRealPathFromURI( Uri contentUri) {//content://media/external/images/media/4288
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String dd = cursor.getString(column_index);
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "sa";

    }


    private String getRealPathFromURI_NEW(Uri contentURI) {
        String result = null;

        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }


    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    /**
     * BottomSheetAdapter
     */


}