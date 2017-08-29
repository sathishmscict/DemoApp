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
import com.myoffersapp.adapter.OfferHistoryAdapterRecyclerView;
import com.myoffersapp.adapter.ReferralListAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.OffersHistoryData;
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


public class OfferHistoryFragment extends Fragment {


    @BindView(R.id.tvNodata)
    TextView tvNodata;

    @BindView(R.id.rvOfferList)
    RecyclerView rvOfferList;
    private Context context = getActivity();
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private SQLiteDatabase sd;
    private String TAG = OfferHistoryFragment.class.getSimpleName();
    private SpotsDialog pDialog;
    private SessionManager sessionManager;
    private SpotsDialog spotsDialog;
    private Unbinder unbinder;

    public OfferHistoryFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_offers_list, container, false);


        unbinder = ButterKnife.bind(this, rootView);


        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getSessionDetails();


        spotsDialog = new SpotsDialog(getActivity());
        // spotsDialog.setCancelable(false);


        LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
        rvOfferList.setLayoutManager(lManager);

        getOfferHistoryDetaislFromServer();


        return rootView;

    }

    private void getOfferHistoryDetaislFromServer() {
        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL  ViewOfferHistoryData  : " + AllKeys.WEBSITE + "ViewOfferHistoryData?type=offerhistory&userid= " + userDetails.get(SessionManager.KEY_USER_ID));


        apiService.getOfferHistoryDetailsFromServer("offerhistory", userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<OffersHistoryData>() {

            //    apiService.getOfferHistoryDetailsFromServer("offerhistory", "5").enqueue(new Callback<OffersHistoryData>() {


            @Override
            public void onResponse(Call<OffersHistoryData> call, retrofit2.Response<OffersHistoryData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.raw().body().toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false) {

                        if (record_status == true) {

                            List<OffersHistoryData.Datum> data = response.body().getData();
                            OfferHistoryAdapterRecyclerView adapter = new OfferHistoryAdapterRecyclerView(context, data);
                            rvOfferList.setAdapter(adapter);

                            rvOfferList.setVisibility(View.VISIBLE);
                            tvNodata.setVisibility(View.GONE);

                        } else {
                            rvOfferList.setVisibility(View.GONE);
                            tvNodata.setVisibility(View.VISIBLE);
                            tvNodata.setText(getString(R.string.str_no_offer_history_found));


                        }
                    }


                } else {

                    Toast.makeText(context, "Something is wrong,try again  Error code :" + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<OffersHistoryData> call, Throwable t) {

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
