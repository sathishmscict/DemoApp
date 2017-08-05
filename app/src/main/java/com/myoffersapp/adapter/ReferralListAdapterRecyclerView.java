package com.myoffersapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.DisplayDealsAcivity;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.helper.AllKeys;
import com.myoffersapp.helper.CommonMethods;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.ReferralData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class ReferralListAdapterRecyclerView extends RecyclerView.Adapter<ReferralListAdapterRecyclerView.MyViewHolder> {

    public String TAG = ReferralListAdapterRecyclerView.class.getSimpleName();

    private  Context _context;
    private  SessionManager sessionManager;

    private List<ReferralData.Datum> list_referaldata;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvreferUserName,tvreferUserMobile;
        private final Button btnClaimFreeCoupon;
        public TextView tvreferDate;

        public MyViewHolder(View view) {
            super(view);


            tvreferDate = (TextView) view.findViewById(R.id.tvreferDate);
            tvreferUserName = (TextView) view.findViewById(R.id.tvreferUserName);
            tvreferUserMobile = (TextView) view.findViewById(R.id.tvreferUserMobile);
            btnClaimFreeCoupon = (Button)view.findViewById(R.id.btnClaimFreeCoupon);



        }

    }


    public ReferralListAdapterRecyclerView(Context context, List<ReferralData.Datum> catList) {
        this.list_referaldata = catList;
        this._context = context;

        sessionManager = new SessionManager(_context);



    }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_single_referral_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ReferralData.Datum
                referalData = list_referaldata.get(position);

        holder.tvreferDate.setText(referalData.getCreateat());
        holder.tvreferUserName.setText(referalData.getName());
        holder.tvreferUserMobile.setText(referalData.getMobile());


        if(referalData.getIsfirstbill().equals("0"))
        {
            holder.btnClaimFreeCoupon.setText("Refered -> Get Coupon when they do first transaction");

        }
        else
        {
            holder.btnClaimFreeCoupon.setText("Claim Free coupon -> Claimed");

        }

        holder.btnClaimFreeCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(referalData.getIsfirstbill().equals("1"))
                {

                    if(referalData.getIsfreecouponselected().equals("0"))
                    {
                        sessionManager.setOfferTypeId(CommonMethods.OFFER_TYPE_REFERAL);
                        sessionManager.setReferalID(referalData.getReferalid());
                        Intent intent = new Intent(_context, DisplayDealsAcivity.class);
                        intent.putExtra(AllKeys.ACTIVITYNAME , TAG);

                        _context.startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(_context, "Free coupon already selected", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    Toast.makeText(_context, "Sorry, you can get free coupon when your friend do first transaction", Toast.LENGTH_SHORT).show();
                }

            }
        });








    }

    @Override
    public int getItemCount() {
        return list_referaldata.size();
    }
}