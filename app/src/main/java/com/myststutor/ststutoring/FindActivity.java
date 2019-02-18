package com.myststutor.ststutoring;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FindActivity extends AppCompatActivity {

    ListView tutorListView;
    TutorListAdapter tutorListAdapter;
    List<Tutor> allTutors = new ArrayList<>();
    List<Tutor> filteredTutors;
    Spinner subjectSpinner;
    String[] subjectArray = {"All", "English", "Math", "Biology", "Computer Science", "History"};

    TextView textViewCurPrice;
    TextView textViewCurAge;
    TextView textViewCurDistance;
    SeekBar seekBarPrice;
    SeekBar seekBarAge;
    SeekBar seekBarDistance;

    LinearLayout tutorLinearyLayout;
    Button editProfileButton;
    Button signoutButton;

    double latitutde;
    double longitude;



    LocationManager locationManager;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("TEST", "Updating the location: " + location.getLatitude() + ", " + location.getLongitude());
                latitutde = location.getLatitude();
                longitude = location.getLongitude();
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.i("TEST", "Permission is given.");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        tutorListView = findViewById(R.id.tutorListView);
        loadTutor();

        signoutButton = findViewById(R.id.signoutButton);
        tutorLinearyLayout = findViewById(R.id.tutorOnlyContainer);
        editProfileButton = findViewById(R.id.editProfileButton);



        String username = Paper.book().read("username", null);
        String password = Paper.book().read("password", null);
        if (username != null && password != null) {
            tutorLinearyLayout.setVisibility(View.VISIBLE);
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindActivity.this, TutorProfileActivity.class);
                    startActivity(intent);
                }
            });
            signoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.book().delete("username");
                    Paper.book().delete("password");
                    Intent intent = new Intent(FindActivity.this, MenuActivity.class);
                    startActivity(intent);

                }
            });
        } else {
            tutorLinearyLayout.setVisibility(View.GONE);
        }


        textViewCurPrice = findViewById(R.id.textViewCurPrice);
        textViewCurAge = findViewById(R.id.textViewCurAge);
        textViewCurDistance = findViewById(R.id.textVIewCurDistance);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        seekBarAge = findViewById(R.id.seekBarAge);
        seekBarDistance = findViewById(R.id.seekBarDistance);

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                textViewCurDistance.setText(i+"");
                filteredTutors = new ArrayList<>();
                for (Tutor tutor : allTutors) {
                    double distance = GeoHelper.getDistance(longitude, latitutde, tutor.getLon(), tutor.getLat());
                    Log.i("Test", "distance: "+ distance);
                    if (distance<=i) {
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
        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                textViewCurAge.setText(i+"");
                filteredTutors = new ArrayList<>();
                for (Tutor tutor : allTutors) {
                    if (tutor.getAgeRangeMin()<=i&&tutor.getAgeRangeMax()>=i) {
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

