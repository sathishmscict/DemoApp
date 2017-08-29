package com.myoffersapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.VendorsActivity;
import com.myoffersapp.adapter.CategoryAdapterRecyclerView;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.SlidersData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener {


    private Context context = getActivity();
    private HashMap<String, String> userDetails = new HashMap<String, String>();


    private SQLiteDatabase sd;

    private int count = 1;
    private TextView nodata;


    private String TAG = HomeFragment.class.getSimpleName();
    private SpotsDialog pDialog;


    private SessionManager sessionManager;

    HashMap<String, String> url_maps = new HashMap<String, String>();

    private ArrayList<Integer> list_SliderId = new ArrayList<Integer>();
    private ArrayList<String> list_SliderImages = new ArrayList<String>();

    private ArrayList<Integer> list_SliderCategoryId = new ArrayList<Integer>();


    private ArrayList<String> list_SliderCategoryURL = new ArrayList<String>();

    private ImageView imgPickup;
    private TextView txtwait;


    @BindView(R.id.final_slider1)
    SliderLayout mDemoSlider;

    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    private SpotsDialog spotsDialog;
    private Unbinder unbinder;

    List<CategoryData.Datum> list_Vendordata;


    public HomeFragment() {
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

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        unbinder = ButterKnife.bind(this, rootView);


        sessionManager = new SessionManager(getActivity());
        userDetails = sessionManager.getSessionDetails();


        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.setCancelable(false);


        pDialog = new SpotsDialog(getActivity());
        pDialog.setCancelable(false);

        //mDemoSlider = (SliderLayout) rootView.findViewById(R.id.final_slider1);

        // rvCategory =

        try {
            LinearLayoutManager lManager = new LinearLayoutManager(getActivity());
            GridLayoutManager gManager = new GridLayoutManager(getActivity(), 3);
            rvCategory.setLayoutManager(gManager);
        } catch (Exception e) {
            e.printStackTrace();
        }


        getCategoryDetailsFromServer();
        //getSlidersByTypeDetailsFromServer();


        //Handle Recyclerview click
        rvCategory.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvCategory, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {

                    Intent intent = new Intent(getActivity(), VendorsActivity.class);
                   // Intent intent = new Intent(getActivity(), NewSingleDealActivity.class);
                    intent.putExtra("VENDORTYPE", "vendor");
                    sessionManager.setCategoryDetails(list_Vendordata.get(position).getCategoryid(), list_Vendordata.get(position).getCategoryname());


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


        return rootView;

    }

    private void getCategoryDetailsFromServer() {

        CommonMethods.showDialog(spotsDialog);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL : " + AllKeys.WEBSITE + "ViewCategoryData?type=category");
        apiService.getAllCategoryDetailsFromServer("category").enqueue(new Callback<CategoryData>() {
            @Override
            public void onResponse(Call<CategoryData> call, retrofit2.Response<CategoryData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "API Called Success" + response.toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false)
                    {

                        if (record_status == true) {


                            list_Vendordata = response.body().getData();
                            CategoryAdapterRecyclerView adapter = new CategoryAdapterRecyclerView(getActivity(), list_Vendordata);
                            rvCategory.setAdapter(adapter);
                            //CommonMethods.hideDialog(spotsDialog);
                            getSlidersByTypeDetailsFromServer();
                        }
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<CategoryData> call, Throwable t) {


                Toast.makeText(getActivity(), "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
                Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
                Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
                Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());


                if (t.getMessage().equals("timeout")) {
                    getCategoryDetailsFromServer();
                } else {

                    CommonMethods.hideDialog(spotsDialog);
                }


            }
        });


    }



    private void getSlidersByTypeDetailsFromServer() {
        CommonMethods.showDialog(spotsDialog);


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "URL : " + AllKeys.WEBSITE + "ViewBanner?type=banner&bannertype=category&categoryid=0&vendorid=0");
        apiService.getSlidersDataFromServer("banner", "category", "0", "0").enqueue(new Callback<SlidersData>() {
            @Override
            public void onResponse(Call<SlidersData> call, retrofit2.Response<SlidersData> response) {

                Log.d(TAG, "Response Code : " + response.code());
                Log.d(TAG, "ViewBanner API Called Success" + response.toString());


                if (response.code() == 200) {

                    String str_error = response.body().getMESSAGE();
                    String str_error_original = response.body().getORIGINALERROR();
                    boolean error_status = response.body().getERRORSTATUS();
                    boolean record_status = response.body().getRECORDS();


                    if (error_status == false) {

                        if (record_status == true) {


                            url_maps.clear();
                            list_SliderId.clear();
                            list_SliderImages.clear();

                            list_SliderCategoryURL.clear();
                            list_SliderCategoryId.clear();
                            List<SlidersData.Datum> arr = response.body().getData();

                            for (int i = 0; i < arr.size(); i++) {
                                //JSONObject c = arr.getJSONObject(i);

                                String url_image_url = arr.get(i).getImg();
                                //c.getString(AllKeys.TAG_BANNER_IMAGE_URL);

                                url_maps.put(String.valueOf(i), url_image_url);
                                list_SliderId.add(i);

                                list_SliderImages.add(url_image_url);

                                list_SliderCategoryId.add(Integer.parseInt(arr.get(i).getCategoryid()));


                            }
                            FillDashBoardSliders();


                        }
                    }


                } else {
                    Toast.makeText(context, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<SlidersData> call, Throwable t) {


                Toast.makeText(getActivity(), "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
                Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
                Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
                Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());


                if (t.getMessage().equals("timeout")) {
                    // getCategoryDetailsFromServer();
                }

                CommonMethods.hideDialog(spotsDialog);
            }
        });






       /* final String url_getSldierByType = AllKeys.WEBSITE + "ViewBanner?type=banner&bannertype=category&categoryid=0&vendorid=0";
        Log.d(TAG, "url ViewBanner : " + url_getSldierByType);
        StringRequest str_getSlidersByType = new StringRequest(Request.Method.GET, url_getSldierByType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Resposne ViewBanner : " + response);


                if (response.contains("img")) {


                    try {
                        //response = AllKeys.convertToJsonFormat(response);
                        JSONObject obj = new JSONObject(response);
                        JSONArray arr = obj.getJSONArray(AllKeys.ARRAY_DATA);
                        url_maps.clear();
                        list_SliderId.clear();
                        list_SliderImages.clear();

                        list_SliderCategoryURL.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject c = arr.getJSONObject(i);

                            String url_image_url = c.getString(AllKeys.TAG_BANNER_IMAGE_URL);
                            url_maps.put(String.valueOf(i), url_image_url);
                            list_SliderId.add(i);
                            list_SliderImages.add(url_image_url);


                        }
                        FillDashBoardSliders();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        hideDialog();
                    }


                }


                hideDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "Error getSlidersByType : " + error.getMessage());
                if (error instanceof ServerError) {
                    hideDialog();
                } else {
                    getSlidersByTypeDetailsFromServer();

                }

            }

        });
    *//*    str_getSlidersByType.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*//*

        MyApplication.getInstance().addToRequestQueue(str_getSlidersByType);*/

    }

    private void FillDashBoardSliders() {


        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setOnSliderClickListener(HomeFragment.this)
                    /*.image(url_maps.get(name))*/
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            ;
            //.setOnSliderClickListener(getActivity());

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            String sd2 = list_SliderImages.get(list_SliderImages.indexOf(url_maps.get(name)));


            int sd = list_SliderId.indexOf(list_SliderImages.indexOf(url_maps.get(name)));

            try {
                mDemoSlider.addSlider(textSliderView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if ((list_SliderId.get(list_SliderImages.indexOf(url_maps.get(name))) % 2) != 0) {
                mDemoSlider.addSlider(textSliderView);
            } else {
                mDemoSlider2.addSlider(textSliderView);

            }
*/

        }


      /*  for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
                 *//*   .setOnSliderClickListener(Newthis);*//*

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
            mDemoSlider2.addSlider(textSliderView);
        }*/

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);


//        mDemoSlider.addOnPageChangeListener(ge);
        // hideDialog();

    }

    public void showDialog() {

        try {
            if (!pDialog.isShowing()) {

                pDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void hideDialog() {
        try {
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            pDialog.cancel();
            pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        if (list_SliderId.size() > 0) {

            int id = list_SliderId.indexOf(Integer.parseInt(slider.getBundle().get("extra").toString()));

           /* String catid = list_SliderCategoryURL.get(id);
            catid = catid.substring(catid.lastIndexOf("=") + 1, catid.length());
            Integer.parseInt(catid)*/
            ;

            try {

                Intent intent = new Intent(getActivity(),
                        VendorsActivity.class);
                intent.putExtra("VENDORTYPE", "vendor");

                sessionManager.setCategoryDetails(String.valueOf(list_SliderCategoryId.get(id)), "");


                intent.putExtra("ActivityName", TAG);

                startActivity(intent);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    /**
     * Produc Review Related Pojo Class
     */


}
