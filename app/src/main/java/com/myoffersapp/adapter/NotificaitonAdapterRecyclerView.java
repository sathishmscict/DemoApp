package com.myoffersapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.myoffersapp.R;
import com.myoffersapp.realm.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class NotificaitonAdapterRecyclerView extends RecyclerView.Adapter<NotificaitonAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private List<Notification> notifiationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivLogo;
        public TextView notificationid,txttitle,txtmessage,txtdate;

        public MyViewHolder(View view) {
            super(view);

            txttitle= (TextView) view.findViewById(R.id.txtheading);
            txtmessage= (TextView) view.findViewById(R.id.txtnote);
            txtdate = (TextView) view.findViewById(R.id.txtdate);
            ivLogo  =(ImageView)view.findViewById(R.id.ivLogo);




        }

    }


    public NotificaitonAdapterRecyclerView(Context context , List<Notification> notificationList) {
        this._context = context;
        this.notifiationList = notificationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification noti = notifiationList.get(position);

        holder.txttitle.setText(""+noti.getTitle());
        holder.txtmessage.setText(""+noti.getDescr());
        holder.txtdate.setText(""+noti.getDate());



        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(noti.getImageURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.ivLogo);


        } catch (Exception e) {
            e.printStackTrace();
        }





    }

    @Override
    public int getItemCount() {
        return notifiationList.size();
    }
}