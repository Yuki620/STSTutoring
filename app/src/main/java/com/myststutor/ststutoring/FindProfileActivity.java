package com.myststutor.ststutoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FindProfileActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView schoolTextView;
    TextView gradeTextView;
    TextView ageTextView;
    TextView availabilityTextView;
    TextView locationTextView;
    TextView introTextView;
    TextView contactTextView;
    TextView findProfilePrice;
    ImageView profileImageView;
    ListView reviewListView;
    EditText reviewEditText;
    RatingBar reviewRatingBar;
    Button submitReviewButton;
    List<Review> reviewList;
    ReviewListAdapter reviewListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findprofile);

        nameTextView = findViewById(R.id.nameFindTextView);
        schoolTextView = findViewById(R.id.schoolFindTextView);
        gradeTextView = findViewById(R.id.gradeFindTextView);
        ageTextView = findViewById(R.id.targetFindTextView);
        availabilityTextView = findViewById(R.id.availabilityFindTextView);
        locationTextView = findViewById(R.id.locationFindTextView);
        introTextView = findViewById(R.id.introFindTextView);
        contactTextView = findViewById(R.id.contactFindTextView);
        findProfilePrice = findViewById(R.id.findProfilePrice);
        profileImageView = findViewById(R.id.imageFindTextView);
        reviewEditText = findViewById(R.id.reviewEditText);
        reviewListView = findViewById(R.id.reviewListView);
        reviewRatingBar = findViewById(R.id.reviewRatingBar);
        submitReviewButton = findViewById(R.id.submitReviewButton);


        loadUser();
        loadReviews();
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = reviewRatingBar.getRating();
                String comments = reviewEditText.getText().toString();
                long timestamp = System.currentTimeMillis();
                Review review = new Review();
                review.setRating(rating);
                review.setComment(comments);
                review.setTimestamp(timestamp);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("review").child(UserManager.selectedTutor.getUid() + "/" + timestamp).setValue(review);

            }
        });

    }

    private void loadUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutor/" + UserManager.selectedTutor.getUid());
        Log.i("Test", UserManager.selectedTutor.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                Log.i("Test", tutor.toString());
                if (tutor != null) {
                    nameTextView.setText(tutor.getName());
                    schoolTextView.setText(tutor.getSchool());
                    gradeTextView.setText(tutor.getGrade() + "");
                    ageTextView.setText(tutor.getAgeRangeMin() + " - " + tutor.getAgeRangeMax());
                    availabilityTextView.setText(tutor.getAvailability());
                    locationTextView.setText(tutor.getLocation());
                    introTextView.setText(tutor.getIntro());
                    contactTextView.setText(tutor.getContact());
                    findProfilePrice.setText("$" + tutor.getPrice() + " per hour");
                    if (tutor.getProfileImageUrl() != null) {
                        Picasso.get().load(tutor.getProfileImageUrl()).into(profileImageView);

                    }

                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Test", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseReference.addValueEventListener(postListener);
    }

    private void loadReviews() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("review/" + UserManager.selectedTutor.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                reviewList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Review r = data.getValue(Review.class);
                    reviewList.add(r);
                }
                Log.i("TEST", "Review Size: " + reviewList.size());
                reviewListAdapter = new ReviewListAdapter(FindProfileActivity.this, R.layout.listview_item_review, reviewList);
                reviewListView.setAdapter(reviewListAdapter);
                setListViewHeightBasedOnChildren(reviewListView);

                // ...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        databaseReference.addValueEventListener(postListener);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
