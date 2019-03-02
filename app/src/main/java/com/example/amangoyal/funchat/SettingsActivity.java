
package com.example.amangoyal.funchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mUser;
    private StorageReference mStorage;

    private ProgressDialog mProgressDialogue;
    public HashMap<String, Object> updateHashMap = new HashMap<>();
    private String downloadURL;
    private String thumb_downloadURL;

    //settings layout
    private TextView mName;
    private TextView mStatus;
    private CircleImageView mImage;
    private Button mChangeStatus, mChangeImage;
    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Setting progress bar
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
        // getting the database refrence from the user's database through current UID
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUid);
        mUserDatabase.keepSynced(true);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                final String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);
                if (!thumbImage.equals("default")) {
                    Picasso.get().load(thumbImage).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(thumbImage).placeholder(R.drawable.default_avatar).into(mImage);

                        }
                    });

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

        /* When the change image button is clicked then gallery intent opens to select the image */
        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProgressDialogue.show();
        if (resultCode == RESULT_OK && requestCode == GALLERY_PICK) {
            Uri imageUri = data.getData();

            //Crop image library used to crop the image startActivityForResult() method
            // return the selected image uri which is passed to cropImage method
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

                File thumb_file = new File(resultUri.getPath());

                // compressing the selected image file
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // It allows us to convert data in to byte form which is used to upload in firebase database
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                final StorageReference filepath = mStorage.child("profile_images").child(mUser.getUid() + ".jpg");

                final StorageReference thumb_filepath = mStorage.child("profile_images").child("thumbs").child(mUser.getUid() + ".jpg");


                // putting image in to firebase database by putFile method of firebase storage
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            mStorage.child("profile_images/" + mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    downloadURL = uri.toString(); /// The string(file link) that you need

                                    //Uploading bitmap
                                    UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {


                                            if (thumb_task.isSuccessful()) {

                                                mStorage.child("profile_images/thumbs/" + mUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        thumb_downloadURL = uri.toString();


                                                        //updating values pf thumb_image and image in firebase database using hashmap
                                                        updateHashMap.put("thumb_image", thumb_downloadURL);
                                                        updateHashMap.put("image", downloadURL);

                                                        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    mProgressDialogue.dismiss();
                                                                    Toast.makeText(SettingsActivity.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });

                                                mProgressDialogue.dismiss();
                                            } else {
                                                Toast.makeText(SettingsActivity.this, "Error in uploading the thumbnail", Toast.LENGTH_SHORT).show();
                                                mProgressDialogue.dismiss();
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
                Exception er;

                er = result.getError();
            }
        }
    }
}
