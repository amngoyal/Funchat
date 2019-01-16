
package com.example.amangoyal.funchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mUser;
    private StorageReference mStorage;

    private ProgressDialog mProgressDialogue;

    String downloadURL;

    //settings layout
    private TextView mName;
    private TextView mStatus;
    private CircleImageView mImage;
    private Button mChangeStatus, mChangeImage;
    private static final int GALLRY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mProgressDialogue = new ProgressDialog(this);
        mProgressDialogue.setTitle("Opening you account settings");
        mProgressDialogue.setMessage("Please wait for a moment");
        mProgressDialogue.setCanceledOnTouchOutside(false);
        mProgressDialogue.setCancelable(false);
        mProgressDialogue.show();


        //initializing all layout elements
        mName = findViewById(R.id.display_name);
        mImage = findViewById(R.id.circle_image);
        mStatus = findViewById(R.id.status);
        mChangeImage = findViewById(R.id.settings_image_button);
        mChangeStatus = findViewById(R.id.settings_status_button);


        // getting database reference
        mStorage = FirebaseStorage.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
                if(!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(mImage);
                }
                mProgressDialogue.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, StatusActivity.class);
                intent.putExtra("status_key", mStatus.getText().toString());
                startActivity(intent);
            }
        });


        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLRY_PICK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GALLRY_PICK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialogue.setTitle("Uploading Image");
                mProgressDialogue.setMessage("Please wait while we upload and process the image");
                mProgressDialogue.setCancelable(false);
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();

                Uri resultUri = result.getUri();

                final StorageReference filepath = mStorage.child("profile_images").child(mUser.getUid() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            mStorage.child("profile_images/" + mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    downloadURL = uri.toString(); /// The string(file link) that you need
                                    mUserDatabase.child("image").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgressDialogue.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Succesfully uploaded", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }
                            });


                        } else
                            Toast.makeText(SettingsActivity.this, "Error in uploding", Toast.LENGTH_SHORT).show();
                        mProgressDialogue.dismiss();
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
