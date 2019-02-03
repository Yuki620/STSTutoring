package com.myststutor.ststutoring;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReviewListAdapter extends ArrayAdapter<Review> {

    List<Review> reviewList;
    Context context;
    int itemLayoutResource;

    public ReviewListAdapter(Context context, int resource, List<Review> objects) {
        super(context, resource, objects);
        this.context=context;
        this.itemLayoutResource=resource;
        this.reviewList=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(itemLayoutResource,parent, false);
        Review r = reviewList.get(position);
        RatingBar reviewItemRatingBar = view.findViewById(R.id.reviewItemRatingBar);
        reviewItemRatingBar.setRating(r.getRating());
        TextView reviewDateItemTextView = view.findViewById(R.id.reviewDateItemTextView);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(r.getTimestamp());
        String dataStr = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        reviewDateItemTextView.setText(dataStr);
        TextView reviewCommentItemTextView = view.findViewById(R.id.reviewCommentItemTextView);
        reviewCommentItemTextView.setText(r.getComment());



        return view;
    }
}
