package com.myststutor.ststutoring;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindActivity extends AppCompatActivity {

    ListView tutorListView;
    TutorListAdapter tutorListAdapter;
    List<Tutor>allTutors=new ArrayList<>();
    List<Tutor>filteredTutors;
    Spinner subjectSpinner;
    String [] subjectArray = {"All", "English", "Math", "Biology", "Computer Science","History"};

    TextView textViewCurPrice;
    TextView textViewCurAge;
    SeekBar seekBarPrice;
    SeekBar seekBarAge;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        tutorListView = findViewById(R.id.tutorListView);
        loadTutor();

        textViewCurPrice = findViewById(R.id.textViewCurPrice);
        textViewCurAge = findViewById(R.id.textViewCurAge);
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                textViewCurPrice.setText("$" + i);
                filteredTutors = new ArrayList<>();
                for (Tutor tutor : allTutors) {
                    if (tutor.getPrice()<=i) {
                        filteredTutors.add(tutor);
                    }
                }
                tutorListAdapter = new TutorListAdapter(FindActivity.this, R.layout.listview_item_tutor, filteredTutors);
                tutorListView.setAdapter(tutorListAdapter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectArray));
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (i>0) {
                    String selectedSubject = subjectArray[i].toLowerCase();
                    filteredTutors = new ArrayList<>();
                    for (Tutor tutor : allTutors) {
                        if (tutor.getSchool() !=null && tutor.getSchool().toLowerCase().contains(selectedSubject)) {
                            filteredTutors.add(tutor);
                        }
                    }
                    tutorListAdapter = new TutorListAdapter(FindActivity.this, R.layout.listview_item_tutor, filteredTutors);
                    tutorListView.setAdapter(tutorListAdapter);
                } else {
                    tutorListAdapter = new TutorListAdapter(FindActivity.this, R.layout.listview_item_tutor, allTutors);
                    tutorListView.setAdapter(tutorListAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void loadTutor() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tutor/");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                allTutors = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Tutor tutor = data.getValue(Tutor.class);
                    allTutors.add(tutor);
                }
                Log.i("test", allTutors.size() + "   " + allTutors.get(0));
                tutorListAdapter = new TutorListAdapter(FindActivity.this, R.layout.listview_item_tutor, allTutors);
                tutorListView.setAdapter(tutorListAdapter);
                tutorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        UserManager.selectedTutor = allTutors.get(i);
                        Intent intent = new Intent(FindActivity.this, FindProfileActivity.class);
                        startActivity(intent);
                    }
                });



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

