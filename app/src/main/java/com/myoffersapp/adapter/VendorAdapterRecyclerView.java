package com.myoffersapp.adapter;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.helper.GPSTracker;
import com.myoffersapp.model.VendorData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class VendorAdapterRecyclerView extends RecyclerView.Adapter<VendorAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private final Location myLocation;
    private final String vendorType;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<VendorData.Datum> list_vendor;
    private ImageLoader mImageLoader;
    private String TAG = VendorAdapterRecyclerView.class.getSimpleName();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivVendorLogo;
        private final TextView tvVendorAddress;
        private final TextView tvDistanceInkm;
        private final TextView tvOfferCount;
        public TextView tvVendorName;

        public MyViewHolder(View view) {
            super(view);


            tvVendorName = (TextView) view.findViewById(R.id.tvVendorName);
            tvVendorAddress = (TextView) view.findViewById(R.id.tvVendorAddress);


            ivVendorLogo = (ImageView) view.findViewById(R.id.ivVendorLogo);
            tvDistanceInkm = (TextView) view.findViewById(R.id.tvDistanceInkm);

            tvOfferCount = (TextView) view.findViewById(R.id.tvOfferCount);


        }

    }


    public VendorAdapterRecyclerView(Context context, List<VendorData.Datum> catList, String vendorType) {
        this.list_vendor = catList;
        this._context = context;
        this.vendorType = vendorType;


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


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (vendorType.toLowerCase().contains("all")) {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_single_vendor_list_all, parent, false);

        } else {

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_single_vendor_list_category, parent, false);

        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VendorData.Datum
                venodrData = list_vendor.get(position);

        holder.tvVendorName.setText(venodrData.getName());

        holder.tvVendorAddress.setText(venodrData.getAddress());


        if (venodrData.getOffercount().equals("0")) {

            holder.tvOfferCount.setText("No offers available");

        } else if (venodrData.getOffercount().equals("1")) {

            holder.tvOfferCount.setText(venodrData.getOffercount() + " Offer available");

        } else {
            holder.tvOfferCount.setText(venodrData.getOffercount() + " Offers available");

        }


        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(list_vendor.get(position).getImg())
                    /*.placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)*/
                    .into(holder.ivVendorLogo);


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Location target = new Location("target");
            target.setLatitude(Double.parseDouble(venodrData.getLatitude()));
            target.setLongitude(Double.parseDouble(venodrData.getLongitude()));
            Log.d(TAG, "Name  : " + venodrData.getName() + " : " + venodrData.getAddress() + " : Location in Distance : " + myLocation.distanceTo(target));

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

    }

    @Override
    public int getItemCount() {
        return list_vendor.size();
    }
}