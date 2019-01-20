package com.myststutor.ststutoring;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.w3c.dom.Text;

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



        loadUser();

    }
    private void loadUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutor/"+ UserManager.selectedTutor.getUid());
        Log.i("Test",UserManager.selectedTutor.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                Log.i("Test", tutor.toString());
                if (tutor != null) {
                    nameTextView.setText(tutor.getName());
                    schoolTextView.setText(tutor.getSchool());
                    gradeTextView.setText(tutor.getGrade()+"");
                    ageTextView.setText(tutor.getAgeRangeMin()+" - " +tutor.getAgeRangeMax());
                    availabilityTextView.setText(tutor.getAvailability());
                    locationTextView.setText(tutor.getLocation());
                    introTextView.setText(tutor.getIntro());
                    contactTextView.setText(tutor.getContact());
                    findProfilePrice.setText("$"+ tutor.getPrice()+" per hour");

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
}
