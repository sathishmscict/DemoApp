package com.myoffersapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.R;
import com.myoffersapp.model.OffersHistoryData;
import com.myoffersapp.model.ReferralData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class OfferHistoryAdapterRecyclerView extends RecyclerView.Adapter<OfferHistoryAdapterRecyclerView.MyViewHolder> {

    private final Context _context;

    private List<OffersHistoryData.Datum> list_referaldata;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvVendorName;
        private final TextView tvVendorAddress;
        private final TextView tvOfferTitle;
        private final TextView txtOfferId;
        private final TextView txtOfferUsedDate;
        private final ImageView ivVendorLogo;
        public TextView tvreferDate;

        public MyViewHolder(View view) {
            super(view);


            tvVendorName = (TextView) view.findViewById(R.id.tvVendorName);

            tvVendorAddress = (TextView) view.findViewById(R.id.tvVendorAddress);
            tvOfferTitle = (TextView) view.findViewById(R.id.tvOfferTitle);
            txtOfferId = (TextView)view.findViewById(R.id.txtOfferId);
            txtOfferUsedDate = (TextView)view.findViewById(R.id.txtOfferUsedData);

            ivVendorLogo = (ImageView)view.findViewById(R.id.ivVendorLogo);








        }

    }


    public OfferHistoryAdapterRecyclerView(Context context, List<OffersHistoryData.Datum> catList) {
        this.list_referaldata = catList;
        this._context = context;




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_offer_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        OffersHistoryData.Datum
                offerhistory = list_referaldata.get(position);






        try {
            /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse("August 21, 2012");
            String formattedDate = targetFormat.format(date);  // 20120821*/

            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(offerhistory.getCreateat());
            String formattedDate = targetFormat.format(date);  // 20120821

            holder.txtOfferUsedDate.setText(Html.fromHtml("DATE : <b>" + formattedDate + "</b>"));
            holder.txtOfferId.setText(Html.fromHtml("ORDER ID : <b>" + offerhistory.getOfferid() + "    "+ formattedDate +"</b>"));

            //holder.txtOfferId.setText(formattedDate+" (" + offerhistory.getOfferid() + ")");

            //holder.txtOfferUsedDate.setText(offerhistory.getCreateat());
            holder.txtOfferUsedDate.setVisibility(View.GONE);

            holder.txtOfferUsedDate.setText("");

           // holder.txtOfferId.setText(" Offer ID : "+offerhistory.getOfferid());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(offerhistory.getOfferimage())
                    /*.placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)*/
                    .into(holder.ivVendorLogo);


        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.tvVendorName.setText(offerhistory.getVendorname());
        holder.tvVendorAddress.setText(offerhistory.getAddress());
        holder.tvOfferTitle.setText(offerhistory.getOffername());








    }

    @Override
    public int getItemCount() {
        return list_referaldata.size();
    }
}