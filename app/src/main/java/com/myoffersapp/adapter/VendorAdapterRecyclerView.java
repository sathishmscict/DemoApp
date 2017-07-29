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
    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<VendorData.Datum> list_vendor;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivVendorLogo;
        private final TextView tvVendorAddress;
        public TextView tvVendorName;

        public MyViewHolder(View view) {
            super(view);


            tvVendorName = (TextView) view.findViewById(R.id.tvVendorName);
            tvVendorAddress = (TextView) view.findViewById(R.id.tvVendorAddress);


            ivVendorLogo = (ImageView) view.findViewById(R.id.ivVendorLogo);


        }

    }


    public VendorAdapterRecyclerView(Context context, List<VendorData.Datum> catList) {
        this.list_vendor = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_vendor_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VendorData.Datum
                venodrData = list_vendor.get(position);

        holder.tvVendorName.setText(venodrData.getName());

        holder.tvVendorAddress.setText(venodrData.getAddress());


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


    }

    @Override
    public int getItemCount() {
        return list_vendor.size();
    }
}