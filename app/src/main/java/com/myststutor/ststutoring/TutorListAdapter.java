package com.myststutor.ststutoring;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TutorListAdapter extends ArrayAdapter<Tutor> {

    List<Tutor> tutorList;
    Context context;
    int itemLayoutResource;

    public TutorListAdapter(Context context, int resource, List<Tutor> objects) {
        super(context, resource, objects);
        this.context=context;
        this.itemLayoutResource=resource;
        this.tutorList=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(itemLayoutResource,parent, false);
        Tutor t = tutorList.get(position);
        Log.i("Test",t.getName());
        TextView nameTextView = view.findViewById(R.id.nameItemTextView);
        nameTextView.setText(t.getName());
        TextView priceTextView = view.findViewById(R.id.priceItemTextView);
        priceTextView.setText("$"+t.getPrice());
        TextView subjectTextView = view.findViewById(R.id.subjectItemTextView);
        subjectTextView.setText(t.getSchool());


        return view;
    }
}
