package com.myststutor.ststutoring;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class TutorProfileActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;


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

    ImageView profilePictureImageView;
    String currentProfileImageUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorprofile);

        nameEditText = findViewById(R.id.editName);
        schoolEditText = findViewById(R.id.editSchool);
        gradeEditText = findViewById(R.id.editGrade);
        ageEditText = findViewById(R.id.editAddress);
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

        profilePictureImageView = findViewById(R.id.profilePicImageView);
        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap b = extras.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DCIM + "/image.jpg";
                    File file = new File(path);
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    b.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    //b.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(path));
                    //String path = MediaStore.Images.Media.insertImage(getContentResolver(), b, "Image Description", null);
                    Uri uri = Uri.parse("file:///" + path);
                    Log.i("TEST", uri.toString() + " size: " + file.length());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] imgdata = baos.toByteArray();

                //mImageView.setImageBitmap(imageBitmap);
                Log.i("TEST", "Successfully took the image! " + b.getByteCount());

                // upload the image to Firebase Storage
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://stst-1d5a6.appspot.com/");
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                // Create a reference to the image file
                final StorageReference imageRef = storageRef.child(
                        "profile-photos/" + System.currentTimeMillis() + ".jpg");

                UploadTask uploadTask = imageRef.putBytes(imgdata );
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.e("TEST", "Failed to upload the photo.");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("TEST", "Successfully uploaded the photo.");
                        // get the URL
                        Task<Uri> task = imageRef.getDownloadUrl();
                        task.addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                Log.i("TEST", "Get the download URL: " + uri.toString());
                                currentProfileImageUrl = uri.toString();
                                Picasso.get().load(currentProfileImageUrl).into(profilePictureImageView);
                            }
                        });
                    }
                });
            }
        }
    }

}
