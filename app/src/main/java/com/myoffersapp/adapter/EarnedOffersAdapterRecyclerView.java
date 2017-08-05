package com.myoffersapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.DashBoardActivity;
import com.myoffersapp.DisplayQRCodeActivity;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.SingleDealDispalyActivity;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.GPSTracker;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.FreeOffersData;
import com.myoffersapp.model.InsertFreeCoupon;
import com.myoffersapp.model.SlidersData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class EarnedOffersAdapterRecyclerView extends RecyclerView.Adapter<EarnedOffersAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private final Location myLocation;
    private final SpotsDialog spotsDialog;

    private double latitude;
    private double longitude;
    private final String TAG = EarnedOffersAdapterRecyclerView.class.getSimpleName();
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<FreeOffersData.Datum> list_deals;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivOfferLogo;
        private final TextView tvOfferTitle, tvOfferDiscount;
        //  private final View viewOfferLine;
        private final TextView tvUnitPrice;
        private final RatingBar ratingBar;
        private final TextView tvDistanceInkm;
        //private final Button btnClaimFreeCoupon;
        private final LinearLayout llDeal;
        private final Button btnView, btnTransfer, btnUse;
        private final TextView tvRemaingCount;


        public MyViewHolder(View view) {
            super(view);


            tvOfferTitle = (TextView) view.findViewById(R.id.tvOfferTitle);
            tvOfferDiscount = (TextView) view.findViewById(R.id.tvOfferDiscount);
            tvUnitPrice = (TextView) view.findViewById(R.id.tvUnitPrice);


            ivOfferLogo = (ImageView) view.findViewById(R.id.ivOfferLogo);

            // viewOfferLine = (View) view.findViewById(R.id.viewOfferLine);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            tvDistanceInkm = (TextView) view.findViewById(R.id.tvDistanceInkm);

            // btnClaimFreeCoupon = (Button) view.findViewById(R.id.btnClaimFreeCoupon);


            //Linear layout used for item click for display free/general coupon
            llDeal = (LinearLayout) view.findViewById(R.id.llDeal);

            btnView = (Button) view.findViewById(R.id.btnView);
            btnTransfer = (Button) view.findViewById(R.id.btnTransfer);
            btnUse = (Button) view.findViewById(R.id.btnUse);

            tvRemaingCount = (TextView)view.findViewById(R.id.tvRemaingCount);



        }

    }


    public EarnedOffersAdapterRecyclerView(Context context, List<FreeOffersData.Datum> catList) {
        this.list_deals = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

        GPSTracker gps = new GPSTracker(_context);

        myLocation = gps.getLocation();

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Current Location : " + "Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        spotsDialog = new SpotsDialog(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_free_deal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final FreeOffersData.Datum
                dealData = list_deals.get(position);

        holder.tvOfferTitle.setText(dealData.getTitle());


        if (dealData.getDiscountypename().toLowerCase().contains("other")) {

            holder.tvOfferDiscount.setVisibility(View.GONE);
            //  holder.viewOfferLine.setVisibility(View.GONE);
            holder.tvUnitPrice.setVisibility(View.GONE);


        } else if (dealData.getDiscountypename().toLowerCase().contains("amount")) {
            // holder.viewOfferLine.setVisibility(View.VISIBLE);
            holder.tvOfferDiscount.setVisibility(View.VISIBLE);
            holder.tvUnitPrice.setVisibility(View.VISIBLE);

            int totalAmount = Integer.parseInt(dealData.getUnitprice());
            int DealAmount = totalAmount - Integer.parseInt(dealData.getDiscountdesc());
            holder.tvUnitPrice.setText("\u20b9" + String.valueOf(totalAmount));
            holder.tvOfferDiscount.setText("\u20b9" + String.valueOf(DealAmount));

        } else if (dealData.getDiscountypename().toLowerCase().contains("percent")) {
            //  holder.viewOfferLine.setVisibility(View.VISIBLE);
            holder.tvOfferDiscount.setVisibility(View.VISIBLE);
            holder.tvUnitPrice.setVisibility(View.VISIBLE);

            int totalAmount = Integer.parseInt(dealData.getUnitprice());
            int DealAmount = (totalAmount * Integer.parseInt(dealData.getDiscountdesc())) / 100;
            DealAmount = totalAmount - DealAmount;


            holder.tvUnitPrice.setText("\u20b9" + String.valueOf(totalAmount));
            holder.tvOfferDiscount.setText("\u20b9" + String.valueOf(DealAmount));

        }


        try {
            holder.ratingBar.setRating(Float.parseFloat(dealData.getRating()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        if(dealData.getIslimited().equals("1"))
        {

            holder.tvRemaingCount.setText("Remaining "+dealData.getRemaining());
            holder.tvRemaingCount.setVisibility(View.VISIBLE);

        }
        else
        {

            holder.tvRemaingCount.setVisibility(View.GONE);

        }


        try
        {
            Location target = new Location("target");
            target.setLatitude(Double.parseDouble(dealData.getLatitude()));
            target.setLongitude(Double.parseDouble(dealData.getLongitude()));
            Log.d(TAG, "Name  : " + dealData.getTitle() + " : " + dealData.getDiscountdesc() + " : Location in Distance : " + myLocation.distanceTo(target));

            Log.d(TAG, "Current Distance  : " + Integer.parseInt(userDetails.get(SessionManager.KEY_DISTANCE_INTERVAL_IN_KM)));

            Log.d(TAG, "Difference  Distance  : " + String.valueOf(myLocation.distanceTo(target)));
            float meters = myLocation.distanceTo(target);

            if (meters > 1000) {
                double conveterDistance = meters / 1000;


                holder.tvDistanceInkm.setText(String.format("%.02f km", conveterDistance));


            } else {
                holder.tvDistanceInkm.setText(String.format("%.02f m", meters));

            }

            holder.tvDistanceInkm.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            holder.tvDistanceInkm.setVisibility(View.GONE);
            e.printStackTrace();
        }


        //Display single offer click on lineatlayout
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new Intent(_context,
                            SingleDealDispalyActivity.class);
                    sessionManager.setOfferDetails(dealData.getOfferid(), dealData.getTitle());
                    intent.putExtra("ActivityName", TAG);
                    _context.startActivity(intent);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        holder.btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(_context, "Please wait...", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(_context, DisplayQRCodeActivity.class);
                _context.startActivity(intent);


            }
        });


        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(list_deals.get(position).getImg())
                    /*.placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)*/
                    .into(holder.ivOfferLogo);


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dealData.getIsused().equals("1")) {

            holder.btnTransfer.setVisibility(View.GONE);
            holder.btnUse.setVisibility(View.GONE);

        } else {
            holder.btnTransfer.setVisibility(View.VISIBLE);
            holder.btnUse.setVisibility(View.VISIBLE);

        }


        holder.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call api for insert free coupon


                final Dialog dialog = new Dialog(_context);
                dialog.setContentView(R.layout.dialog_transfer_coupon);


                TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);
                final EditText edtMobile = (EditText) dialog.findViewById(R.id.edtMobile);
                final TextInputLayout edtMobileWrapper = (TextInputLayout) dialog.findViewById(R.id.edtMobileWrapper);
                final Button btnGift = (Button) dialog.findViewById(R.id.btnTransfer);


                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                        dialog.dismiss();
                    }
                });

                btnGift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edtMobile.getText().toString().equals("")) {
                            edtMobileWrapper.setErrorEnabled(true);
                            edtMobileWrapper.setError("Enter mobile no ");
                        } else if (edtMobile.getText().toString().length() != 10) {

                            edtMobileWrapper.setErrorEnabled(true);
                            edtMobileWrapper.setError("Invalid mobile no ");


                        } else {
                            edtMobileWrapper.setErrorEnabled(false);

                            //Call API for check mobile no. If mobile no exist in our database then coupon has been transfered to given mobile no
                            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            Log.d(TAG , "URL Tranfercoupon  "+ AllKeys.WEBSITE +"TransferFreeCoupon?type=transfercoupon&mobile="+ edtMobile.getText().toString() +"&couponid="+ dealData.getOfferid() +"&userid="+userDetails.get(SessionManager.KEY_USER_ID));

                            apiInterface.giftOrTransferCouponToOtherUser("transfercoupon", edtMobile.getText().toString(), dealData.getOfferid(), userDetails.get(SessionManager.KEY_USER_ID)).enqueue(new Callback<InsertFreeCoupon>() {

                                @Override
                                public void onResponse(Call<InsertFreeCoupon> call, Response<InsertFreeCoupon> response) {

                                    if (response.code() == 200) {
                                        String str_error = response.body().getMESSAGE();
                                        String str_error_original = response.body().getORIGINALERROR();
                                        boolean error_status = response.body().getERRORSTATUS();
                                        boolean record_status = response.body().getRECORDS();


                                        if (error_status == false)
                                        {



                                                dialog.cancel();
                                            holder.btnUse.setVisibility(View.GONE);
                                            holder.btnTransfer.setVisibility(View.GONE);

                                                //Alert Dialog for sucess message
                                                AlertDialog.Builder builder = new AlertDialog.Builder(_context);

                                                builder.setTitle("Coupon info");
                                                builder.setMessage(dealData.getTitle()+" coupon has been successfully transfered to "+edtMobile.getText().toString());

                                                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogg, int which) {




                                                        dialogg.cancel();


                                                    }
                                                });
                                                builder.show();





                                        }
                                        else
                                        {
                                            edtMobileWrapper.setErrorEnabled(true);
                                            edtMobileWrapper.setError(str_error);
                                        }

                                    } else {
                                        CommonMethods.showServerError(_context, response.code());
                                    }

                                }

                                @Override
                                public void onFailure(Call<InsertFreeCoupon> call, Throwable t) {
                                    CommonMethods.displayFailerError(_context,TAG,t);

                                    CommonMethods.hideDialog(spotsDialog);
                                }
                            });


                        }
                    }
                });
                dialog.getWindow().setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                dialog.show();


                if (userDetails.get(SessionManager.KEY_OFFFER_TYPE_ID).equals(String.valueOf(CommonMethods.OFFER_TYPE_REFERAL))) {

                    CommonMethods.showDialog(spotsDialog);
                    //API for inset free coupon
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Log.d(TAG, "URL Insert Free Coupon : " + AllKeys.WEBSITE + "InsertFreeCoupon?type=freecoupon&userid=" + userDetails.get(SessionManager.KEY_USER_ID) + "&couponid=" + dealData.getOfferid() + "&referalid=" + userDetails.get(SessionManager.KEY_REFERALID) + "");
                    apiInterface.insertFreeCouponDataToServer("freecoupon", userDetails.get(SessionManager.KEY_USER_ID), dealData.getOfferid(), userDetails.get(SessionManager.KEY_REFERALID)).enqueue(new Callback<SlidersData>() {
                        @Override
                        public void onResponse(Call<SlidersData> call, Response<SlidersData> response) {


                            if (response.code() == 200) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                                builder.setTitle("Free coupon info");
                                builder.setMessage(dealData.getTitle() + " free coupon has been added to your account.Thank you for using " + _context.getString(R.string.app_name));
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                        dialog.dismiss();

                                        Intent intent = new Intent(_context, DashBoardActivity.class);
                                        _context.startActivity(intent);


                                    }
                                });
                                CommonMethods.hideDialog(spotsDialog);
                                builder.show();


                            } else {
                                CommonMethods.showServerError(_context, response.code());
                            }

                            sessionManager.setReferalID("0");
                        }

                        @Override
                        public void onFailure(Call<SlidersData> call, Throwable t) {

                            CommonMethods.displayFailerError(_context, TAG, t);
                            CommonMethods.hideDialog(spotsDialog);

                        }
                    });


                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list_deals.size();
    }
}