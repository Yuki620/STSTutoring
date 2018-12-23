package com.myststutor.ststutoring;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }
}
