package com.myoffersapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.DealsData;
import com.myoffersapp.model.VendorData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class DealsAdapterRecyclerView extends RecyclerView.Adapter<DealsAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<DealsData.Datum> list_deals;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivOfferLogo;
        private final TextView tvOfferTitle, tvOfferDiscount;
        private final View viewOfferLine;
        private final TextView tvUnitPrice;


        public MyViewHolder(View view) {
            super(view);


            tvOfferTitle = (TextView) view.findViewById(R.id.tvOfferTitle);
            tvOfferDiscount = (TextView) view.findViewById(R.id.tvOfferDiscount);
            tvUnitPrice = (TextView) view.findViewById(R.id.tvUnitPrice);


            ivOfferLogo = (ImageView) view.findViewById(R.id.ivOfferLogo);

            viewOfferLine = (View) view.findViewById(R.id.viewOfferLine);


        }

    }


    public DealsAdapterRecyclerView(Context context, List<DealsData.Datum> catList) {
        this.list_deals = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_deal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DealsData.Datum
                dealData = list_deals.get(position);

        holder.tvOfferTitle.setText(dealData.getTitle());


        if (dealData.getDiscountname().toLowerCase().contains("other")) {

            holder.tvOfferDiscount.setVisibility(View.GONE);
            holder.viewOfferLine.setVisibility(View.GONE);
            holder.tvUnitPrice.setVisibility(View.GONE);


        } else if (dealData.getDiscountname().toLowerCase().contains("amount")) {
            holder.viewOfferLine.setVisibility(View.VISIBLE);
            holder.tvOfferDiscount.setVisibility(View.VISIBLE);
            holder.tvUnitPrice.setVisibility(View.VISIBLE);

            int totalAmount = Integer.parseInt(dealData.getUnitprice());
            int DealAmount = totalAmount - Integer.parseInt(dealData.getDiscountdesc());
            holder.tvUnitPrice.setText("\u20b9"+String.valueOf(totalAmount));
            holder.tvOfferDiscount.setText("\u20b9"+String.valueOf(DealAmount));

        } else if (dealData.getDiscountname().toLowerCase().contains("percent")) {
            holder.viewOfferLine.setVisibility(View.VISIBLE);
            holder.tvOfferDiscount.setVisibility(View.VISIBLE);
            holder.tvUnitPrice.setVisibility(View.VISIBLE);

            int totalAmount = Integer.parseInt(dealData.getUnitprice());
            int DealAmount = (Integer.parseInt(dealData.getDiscountdesc()) * 100) / totalAmount;

            holder.tvUnitPrice.setText("\u20b9"+String.valueOf(totalAmount));
            holder.tvOfferDiscount.setText("\u20b9"+String.valueOf(DealAmount));

        }


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