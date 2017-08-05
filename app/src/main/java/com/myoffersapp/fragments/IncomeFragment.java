package com.myoffersapp.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.adapter.EarnedOffersAdapterRecyclerView;
import com.myoffersapp.adapter.ReferralListAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.FreeOffersData;
import com.myoffersapp.model.ReferralData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class IncomeFragment extends Fragment {


    @BindView(R.id.tvNodata)
    TextView tvNodata;

    @BindView(R.id.rvReferrals)
    RecyclerView rvReferrals;
    private Context context = getActivity();
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SQLiteDatabase sd;
    private String TAG = IncomeFragment.class.getSimpleName();
    private SpotsDialog pDialog;
    private SessionManager sessionManager;
    private SpotsDialog spotsDialog;
    private Unbinder unbinder;

    public IncomeFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_referrals, container, false);


        unbinder = ButterKnife.bind(this, rootView);


        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getSessionDetails();


        spotsDialog = new SpotsDialog(getActivity());
        // spotsDialog.setCancelable(false);


        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        rvReferrals.setLayoutManager(lManager);

        getIncomeDetaislFromServer();



        return rootView;

    }

    private void getIncomeDetaislFromServer() {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG , "Params  "+ AllKeys.WEBSITE +"ViewFreeOffersData?type=viewfreeoffers&userid= "+userDetails.get(SessionManager.KEY_USER_ID));
        /*apiService.getAllReferralsDetailsFromServer("referal", userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<ReferralData>() {*/
        apiService.getAllFreeOffersData("viewfreeoffers", userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<FreeOffersData>() {

            @Override
            public void onResponse(Call<FreeOffersData> call, retrofit2.Response<FreeOffersData> response) {

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

                            List<
                                    FreeOffersData.Datum> data = response.body().getData();
                            EarnedOffersAdapterRecyclerView adapter = new EarnedOffersAdapterRecyclerView(getActivity(), data);
                            rvReferrals.setAdapter(adapter);
                            CommonMethods.hideDialog(spotsDialog);

                            rvReferrals.setVisibility(View.VISIBLE);
                            tvNodata.setVisibility(View.GONE);

                        }
                        else
                        {
                            rvReferrals.setVisibility(View.GONE);
                            tvNodata.setVisibility(View.VISIBLE);
                            tvNodata.setText(getString(R.string.str_no_earned_offers_found));



                        }
                    }



                } else {

                    Toast.makeText(context, "Something is wrong,try again  Error code :"+response.code(), Toast.LENGTH_SHORT).show();
                }


                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<FreeOffersData> call, Throwable t) {

                Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API." + t.getMessage());

                CommonMethods.hideDialog(spotsDialog);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    /**
     * Produc Review Related Pojo Class
     */


}
