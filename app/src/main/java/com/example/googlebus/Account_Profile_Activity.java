package com.example.googlebus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Account_Profile_Activity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvMobileNo, tvEmailId, tvUsername;
    AppCompatButton btnEditProfile, btnSignOut;

    SharedPreferences preferences;
    String stringUsername;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(Account_Profile_Activity.this);

        stringUsername = preferences.getString("username", "");

        ivProfilePhoto = findViewById(R.id.ivMyProfileProfilePhoto);
        btnEditProfile = findViewById(R.id.btnMyProfileEditeProfile);
        tvName = findViewById(R.id.tvMyProfileName);
        tvMobileNo = findViewById(R.id.tvMyProfileMobileNo);
        tvEmailId = findViewById(R.id.tvMyProfileEmailId);
        tvUsername = findViewById(R.id.tvMyProfileUsername);
        btnSignOut = findViewById(R.id.btnMyProfileSingOut);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(Account_Profile_Activity.this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount != null) {
            String name = googleSignInAccount.getDisplayName();
            String email = googleSignInAccount.getEmail();

            tvName.setText(name);
            tvEmailId.setText(email);

            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(Account_Profile_Activity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(Account_Profile_Activity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait.....");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetails();
    }

    private void getMyDetails() {
    }
}

