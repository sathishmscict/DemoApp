package com.myoffersapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.ReferralData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class ReferralListAdapterRecyclerView extends RecyclerView.Adapter<ReferralListAdapterRecyclerView.MyViewHolder> {

    private final Context _context;

    private List<ReferralData.Datum> list_referaldata;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvreferUserName,tvreferUserMobile;
        public TextView tvreferDate;

        public MyViewHolder(View view) {
            super(view);


            tvreferDate = (TextView) view.findViewById(R.id.tvreferDate);
            tvreferUserName = (TextView) view.findViewById(R.id.tvreferUserName);
            tvreferUserMobile = (TextView) view.findViewById(R.id.tvreferUserMobile);


        }

    }


    public ReferralListAdapterRecyclerView(Context context, List<ReferralData.Datum> catList) {
        this.list_referaldata = catList;
        this._context = context;




    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_referral_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ReferralData.Datum
                referalData = list_referaldata.get(position);

        holder.tvreferDate.setText(referalData.getCreateat());
        holder.tvreferUserName.setText(referalData.getName());
        holder.tvreferUserMobile.setText(referalData.getMobile());





    }

    @Override
    public int getItemCount() {
        return list_referaldata.size();
    }
}