package com.example.amangoyal.funchat.loginAndRegisterActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.amangoyal.funchat.MainActivity;
import com.example.amangoyal.funchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "auth";
    private TextInputLayout mName, mEmail, mPass;
    private Button mCreate;
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar toolbar;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Setting v7 toolbar
        toolbar = findViewById(R.id.registertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // getting authentication reference
        mAuth = FirebaseAuth.getInstance();


        //Initializing all the layout parameters
        mName = findViewById(R.id.textInputLayout);
        mEmail = findViewById(R.id.textInputLayout2);
        mPass = findViewById(R.id.textInputLayout3);
        mCreate = findViewById(R.id.regCreate);

        mProgress = new ProgressDialog(this);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String pass = mPass.getEditText().getText().toString();


                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mProgress.setTitle("Creating Your Account");
                    mProgress.setMessage("Please Wait a While");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    // this method will register new user in the firebase
                    registerNewUser(name, email, pass);
                } else
                    Toast.makeText(RegisterActivity.this, "Please fill all the entries", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerNewUser(final String name, String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Getting current user using FirebaseUser
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = firebaseUser.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                            //Adding data to HashMap to set in firabse databse
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("status", "Hi! there I'm using FunChat");
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");
                            userMap.put("tokenID", FirebaseInstanceId.getInstance().getToken());

                            //Setting values in databse using set value function of firebase DB
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        mProgress.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        mProgress.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Database failure", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            mProgress.hide();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
}
