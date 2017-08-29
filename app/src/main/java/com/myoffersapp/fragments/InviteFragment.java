package com.myoffersapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.helper.AllKeys;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class InviteFragment extends Fragment {


    @BindView(R.id.tvReferralCode)
    TextView tvReferralCode;

    @BindView(R.id.btnShare)
    Button btnShare;
    private Context context = getActivity();
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SQLiteDatabase sd;
    private String TAG = InviteFragment.class.getSimpleName();
    private SpotsDialog pDialog;
    private SessionManager sessionManager;
    private SpotsDialog spotsDialog;
    private Unbinder unbinder;

    public InviteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_invite, container, false);


        unbinder = ButterKnife.bind(this, rootView);


        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getSessionDetails();


        spotsDialog = new SpotsDialog(getActivity());
        // spotsDialog.setCancelable(false);


        tvReferralCode.setText(userDetails.get(SessionManager.KEY_USER_REFERAL_CODE));
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>I'm giving you free Offer/Coupon . To accept, use code '"+ userDetails.get(SessionManager.KEY_USER_REFERAL_CODE) +"' to sign up. Enjoy! Details: "+ AllKeys.URL_REFERRAL_URL + userDetails.get(SessionManager.KEY_USER_REFERAL_CODE) +"</p>"));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
*/
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                /*sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I'm giving you free Offer/Coupon . To accept, use code '"+ userDetails.get(SessionManager.KEY_USER_REFERAL_CODE) +"' to sign up. Enjoy! Details: "+ AllKeys.URL_REFERRAL_URL + userDetails.get(SessionManager.KEY_USER_REFERAL_CODE));*/
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "I'm giving you free Offer/Coupon . To accept, use code '"+ userDetails.get(SessionManager.KEY_USER_REFERAL_CODE) +"' to sign up. Enjoy! Details: "+ AllKeys.URL_REFERRAL_URL + userDetails.get(SessionManager.KEY_USER_REFERAL_CODE));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);


            }
        });



        return rootView;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    /**
     * Produc Review Related Pojo Class
     */


}
