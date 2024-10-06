package com.example.googlebus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googlebus.Comman.NetworkChangeListener;
import com.example.googlebus.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etMobileNo, etEmilId, etUsername, etPassword;
    AppCompatButton btnSingUp;
    TextView tvSingIn;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        preferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        editor = preferences.edit();


        etName = findViewById(R.id.etRegisterName);
        etMobileNo = findViewById(R.id.etRegisterMobileNo);
        etEmilId = findViewById(R.id.etRegisterEmailId);
        etUsername = findViewById(R.id.etRegisterUserName);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnSingUp = findViewById(R.id.btnSingUp);
        tvSingIn = findViewById(R.id.tvSingIn);

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Your Name");
                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Please Enter your Mobile Number");
                } else if (etMobileNo.getText().toString().length() != 10) {
                    etMobileNo.setError("Invalid Mobile No");
                } else if (etEmilId.getText().toString().isEmpty()) {
                    etEmilId.setError("Please Enter Your Email Id");
                } else if (!etEmilId.getText().toString().contains("@") ||
                        !etEmilId.getText().toString().contains(".com")) {
                    etEmilId.setError("Please Enter Your Valid Email Id");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Please Enter Your Username");
                } else if (etUsername.getText().toString().length() < 8) {
                    etUsername.setError("Username Must Be Greater 8");
                } else if (!etUsername.getText().toString().matches(".*[a-z].*")) {
                    etUsername.setError("Password Enter At Least One Lowercase Letter");
                } else if (!etUsername.getText().toString().matches(".*[0-9].*")) {
                    etUsername.setError("Password Enter At Least One Number");
                } else if (!etUsername.getText().toString().matches(".*[@,$,%,&,/].*")) {
                    etUsername.setError("Password Enter at least one special character");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("please Enter 8 Digit Character Password");
                } else {
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Registration is in process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    userRegisterDetails();
                }
            }

        });
    }

    private void userRegisterDetails() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name", etMobileNo.getText().toString());
        params.put("Mobileno", etEmilId.getText().toString());
        params.put("enailid", etEmilId.getText().toString());
        params.put("username", etUsername.getText().toString());

        client.post(Urls.registerUserWebService, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration Successfully Done ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Already Data Present", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

          PhoneAuthProvider.getInstance().verifyPhoneNumber(
          "+91" + etMobileNo.getText().toString(),
         60, TimeUnit.SECONDS, RegisterActivity.this,
         new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
             @Override
             public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                 progressDialog.dismiss();
                 Toast.makeText(RegisterActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onVerificationFailed(@NonNull FirebaseException e) {
                 progressDialog.dismiss();
                 Toast.makeText(RegisterActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onCodeSent(@NonNull String verificationCode, @NonNull
             PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                 Intent intent = new Intent(RegisterActivity.this, VerifyOTPActivity.class);
                 intent.putExtra("verificationCode", verificationCode);
                 intent.putExtra("name", etName.getText().toString());
                 intent.putExtra("mobileno", etMobileNo.getText().toString());
                 intent.putExtra("email", etEmilId.getText().toString());
                 intent.putExtra("username", etUsername.getText().toString());
                 intent.putExtra("password", etPassword.getText().toString());
                 startActivity(intent);
             }
          });
    tvSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    });
    }
@Override
         protected void onStart() {
    super.onStart();
         IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
         registerReceiver(networkChangeListener,intentFilter);
           }
        @Override
        public void onStop() {
            super.onStop();
        unregisterReceiver(networkChangeListener);
         }
}