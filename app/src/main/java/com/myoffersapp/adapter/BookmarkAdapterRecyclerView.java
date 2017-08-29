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
import com.myoffersapp.BookmarkActivity;
import com.myoffersapp.DashBoardActivity;
import com.myoffersapp.DisplayQRCodeActivity;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.SingleDealDispalyActivity;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.helper.GPSTracker;
import com.myoffersapp.model.BookmarkData;
import com.myoffersapp.model.CommonReponse;
import com.myoffersapp.model.FreeOffersData;
import com.myoffersapp.model.InsertFreeCoupon;
import com.myoffersapp.model.SlidersData;
import com.myoffersapp.retrofit.ApiClient;
import com.myoffersapp.retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class BookmarkAdapterRecyclerView extends RecyclerView.Adapter<BookmarkAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private final Location myLocation;
    private final SpotsDialog spotsDialog;

    private double latitude;
    private double longitude;
    private final String TAG = BookmarkAdapterRecyclerView.class.getSimpleName();
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<BookmarkData.Datum> list_deals;
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
        private final Button btnView, btnRemove, btnUse;
        private final TextView tvRemaingCount;
        private final TextView tvVendorAddress;
        private final TextView tvVendorName;


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
            btnRemove = (Button) view.findViewById(R.id.btnRemove);
            btnUse = (Button) view.findViewById(R.id.btnUse);

            tvRemaingCount = (TextView) view.findViewById(R.id.tvRemaingCount);

            tvVendorAddress = (TextView) view.findViewById(R.id.tvVendorAddress);
            tvVendorName = (TextView) view.findViewById(R.id.tvVendorName);


        }

    }


    public BookmarkAdapterRecyclerView(Context context, List<BookmarkData.Datum> catList) {
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
                .inflate(R.layout.row_single_bookmark, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final BookmarkData.Datum dealData = list_deals.get(position);

        holder.tvOfferTitle.setText(dealData.getTitle());
        holder.tvVendorAddress.setText(dealData.getAddress());
        holder.tvVendorName.setText(dealData.getVendorname());


        if (dealData.getDiscountname().toLowerCase().contains("other")) {

            holder.tvOfferDiscount.setVisibility(View.GONE);
            //  holder.viewOfferLine.setVisibility(View.GONE);
            holder.tvUnitPrice.setVisibility(View.GONE);


        } else if (dealData.getDiscountname().toLowerCase().contains("amount")) {
            // holder.viewOfferLine.setVisibility(View.VISIBLE);
            holder.tvOfferDiscount.setVisibility(View.VISIBLE);
            holder.tvUnitPrice.setVisibility(View.VISIBLE);

            int totalAmount = Integer.parseInt(dealData.getUnitprice());
            int DealAmount = totalAmount - Integer.parseInt(dealData.getDiscountdesc());
            holder.tvUnitPrice.setText("\u20b9" + String.valueOf(totalAmount));
            holder.tvOfferDiscount.setText("\u20b9" + String.valueOf(DealAmount));

        } else if (dealData.getDiscountname().toLowerCase().contains("percent")) {
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


        holder.tvRemaingCount.setVisibility(View.GONE);
      /*  if (dealData.getIslimited().equals("1")) {

            holder.tvRemaingCount.setText("Remaining " + dealData.getRemaining());
            holder.tvRemaingCount.setVisibility(View.VISIBLE);

        } else {

            holder.tvRemaingCount.setVisibility(View.GONE);

        }
*/

        try {
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

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonMethods.showDialog(spotsDialog);

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Log.d(TAG, "URL removeBookmark : " + AllKeys.WEBSITE + "RemoveBookmark?type=bookmark&bookmarkid="+ dealData.getBookmarkid() +"");
                apiInterface.removeBookmarkFromServer("bookmark", dealData.getBookmarkid()).enqueue(new Callback<CommonReponse>() {
                    @Override
                    public void onResponse(Call<CommonReponse> call, Response<CommonReponse> response) {

                        if (response.code() == 200) {

                            list_deals.remove(position);
                            notifyDataSetChanged();

                            if(list_deals.size() == 0)
                            {
Intent intent = new Intent(_context , BookmarkActivity.class);
                                _context.startActivity(intent);


                            }


                        } else {
                            CommonMethods.showServerError(_context, response.code());

                        }
                        CommonMethods.hideDialog(spotsDialog);
                    }

                    @Override
                    public void onFailure(Call<CommonReponse> call, Throwable t) {


                        CommonMethods.displayFailerError(_context, TAG, t);
                        CommonMethods.hideDialog(spotsDialog);

                    }
                });


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


    }

    @Override
    public int getItemCount() {
        return list_deals.size();
    }
}