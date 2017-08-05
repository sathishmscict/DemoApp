package com.myoffersapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.model.CategoryData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import berlin.volders.badger.BadgeDrawable;
import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class CategoryAdapterRecyclerView extends RecyclerView.Adapter<CategoryAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<CategoryData.Datum> list_categoty;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCategory;
        private final TextView tvCount;
        public TextView tvcategory;

        public MyViewHolder(View view) {
            super(view);


            tvcategory = (TextView) view.findViewById(R.id.tvcategory);

            ivCategory = (ImageView) view.findViewById(R.id.ivCategory);

            tvCount = (TextView)view.findViewById(R.id.tvCount);



        }

    }


    public CategoryAdapterRecyclerView(Context context, List<CategoryData.Datum> catList) {
        this.list_categoty = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_category_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

                CategoryData.Datum
                category = list_categoty.get(position);

        holder.tvcategory.setText(category.getCategoryname());


        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(list_categoty.get(position).getImage())
                    /*.placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)*/
                    .into(holder.ivCategory);




            holder.tvCount.setText(list_categoty.get(position).getOffercount());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list_categoty.size();
    }




}
