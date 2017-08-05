package com.myoffersapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.myoffersapp.R;
import com.myoffersapp.SessionManager;
import com.myoffersapp.model.CategoryData;
import com.myoffersapp.model.ReviewData;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Satish Gadde on 30-04-2016.
 */
public class ReviewAdapterRecyclerView extends RecyclerView.Adapter<ReviewAdapterRecyclerView.MyViewHolder> {

    private final Context _context;
    private final SessionManager sessionManager;

    private HashMap<String, String> userDetails = new HashMap<String, String>();
    private List<ReviewData.Datum> list_reviews;
    private ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircularImageView imgProfilePic;
        private final TextView tvUserName;
        private final TextView tvReviewDate;
        private final EditText edtReview;
        private final RatingBar ratingBar;


        public MyViewHolder(View view) {
            super(view);

            imgProfilePic =(CircularImageView)view.findViewById(R.id.imgProfilePic);
            tvUserName = (TextView)view.findViewById(R.id.tvUserName);



            tvReviewDate = (TextView) view.findViewById(R.id.tvReviewDate);

            edtReview = (EditText) view.findViewById(R.id.edtReview);

            ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);





        }

    }


    public ReviewAdapterRecyclerView(Context context, List<ReviewData.Datum> catList) {
        this.list_reviews = catList;
        this._context = context;

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_single_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ReviewData.Datum
                review = list_reviews.get(position);

        holder.tvUserName.setText(review.getUsername());
        holder.edtReview.setText(review.getDescription());


        try {
            /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = originalFormat.parse("August 21, 2012");
            String formattedDate = targetFormat.format(date);  // 20120821*/

            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(review.getDate());
            String formattedDate = targetFormat.format(date);  // 20120821

            holder.tvReviewDate.setText(Html.fromHtml("<b>" + formattedDate + "</b>"));


            //holder.txtOfferId.setText(formattedDate+" (" + offerhistory.getOfferid() + ")");

            //holder.txtOfferUsedDate.setText(offerhistory.getCreateat());



            // holder.txtOfferId.setText(" Offer ID : "+offerhistory.getOfferid());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            holder.ratingBar.setRating(Float.parseFloat(review.getRating()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        try {
            //Glide.with(_context).load(list_categoty.get(position).getImageurl()).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).crossFade(R.anim.fadein, 2000).override(500, 500).into(holder.ivCategory);

            Picasso.with(_context)
                    .load(review.getUseravatar())
                    .placeholder(R.drawable.icon_userlogo)
                    .error(R.drawable.icon_userlogo)
                    .into(holder.imgProfilePic);







        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list_reviews.size();
    }






}
