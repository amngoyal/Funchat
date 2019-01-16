package com.example.amangoyal.funchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button mStatusChangeButton;
    private TextInputLayout mStatusEditText;
    private DatabaseReference mStatusRefrence;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //layout
        mProgress = new ProgressDialog(this);
        toolbar = findViewById(R.id.staus_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatusChangeButton = findViewById(R.id.staus_change_button);
        mStatusEditText = findViewById(R.id.status_change_TextInputLayout);


        //firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mStatusRefrence = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("status");

        String currentStatus = getIntent().getStringExtra("status_key");
        mStatusEditText.getEditText().setText(currentStatus);


        mStatusChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setTitle("Saving you changes");
                mProgress.setMessage("Please wait for a moment");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setCancelable(false);
                mProgress.show();

                mStatusChangeButton.setEnabled(false);

                String newStatus = mStatusEditText.getEditText().getText().toString();
                mStatusRefrence.setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                            finish();

                        } else {
                            mProgress.hide();
                            mStatusChangeButton.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Error in saving your changes", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });


    }
}
