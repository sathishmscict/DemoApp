package com.myoffersapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.myoffersapp.MyApplication;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.adapter.CategoryAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;


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
