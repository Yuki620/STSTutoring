package com.myststutor.ststutoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;


public class TutorProfileActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText schoolEditText;
    EditText gradeEditText;
    EditText ageEditText;
    EditText availabilityEditText;
    EditText locationEditText;
    EditText introEditText;
    EditText contactEditText;
    Button confirmButton;
    TextView profileCurPrice;
    SeekBar seekBarPrice;
    RangeSeekBar<Integer> tutorProfileTarget;
    TextView profileMinAge;
    TextView profileMaxAge;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorprofile);

        nameEditText = findViewById(R.id.editName);
        schoolEditText = findViewById(R.id.editSchool);
        gradeEditText = findViewById(R.id.editGrade);
        ageEditText = findViewById(R.id.editTarget);
        availabilityEditText = findViewById(R.id.editAvailability);
        locationEditText = findViewById(R.id.editLocation);
        introEditText = findViewById(R.id.editIntro);
        contactEditText = findViewById(R.id.editContact);
        confirmButton = findViewById(R.id.buttonConfirm);
        profileCurPrice = findViewById(R.id.profileCurPrice);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        tutorProfileTarget = findViewById(R.id.tutorProfileTarget);
        profileMinAge = findViewById(R.id.profileMinAge);
        profileMaxAge = findViewById(R.id.profileMaxAge);

        loadUser();

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                profileCurPrice.setText("$" + i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tutorProfileTarget.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                profileMinAge.setText(""+ minValue);
                profileMaxAge.setText(""+ maxValue);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tutor t = new Tutor();
                t.setName(nameEditText.getText().toString());
                t.setSchool(schoolEditText.getText().toString());
                t.setGrade(Integer.parseInt(gradeEditText.getText().toString()));
                t.setAgeRangeMin(Integer.parseInt(profileMinAge.getText().toString()));
                t.setAgeRangeMax(Integer.parseInt(profileMaxAge.getText().toString()));
                t.setAvailability(availabilityEditText.getText().toString());
                t.setLocation(locationEditText.getText().toString());
                t.setIntro(introEditText.getText().toString());
                t.setContact(contactEditText.getText().toString());
                t.setEmail(UserManager.user.getEmail());
                t.setUid(UserManager.user.getUid());
                t.setPrice(seekBarPrice.getProgress());

                // Write a message to the database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child("tutor").child(t.getUid()).setValue(t);
                startActivity(new Intent(TutorProfileActivity.this, FindActivity.class));

            }
        });

    }
    private void loadUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutor/"+ UserManager.user.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Tutor tutor = dataSnapshot.getValue(Tutor.class);
                if (tutor != null) {
                    nameEditText.setText(tutor.getName());
                    schoolEditText.setText(tutor.getSchool());
                    gradeEditText.setText(tutor.getGrade()+"");
                    tutorProfileTarget.setSelectedMinValue(tutor.getAgeRangeMin());
                    tutorProfileTarget.setSelectedMaxValue(tutor.getAgeRangeMax());
                    availabilityEditText.setText(tutor.getAvailability());
                    locationEditText.setText(tutor.getLocation());
                    introEditText.setText(tutor.getIntro());
                    contactEditText.setText(tutor.getContact());
                    seekBarPrice.setProgress((int)(tutor.getPrice()));
                    profileCurPrice.setText("$"+ tutor.getPrice());

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
