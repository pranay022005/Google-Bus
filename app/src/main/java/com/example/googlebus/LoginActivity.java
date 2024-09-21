package com.example.googlebus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.googlebus.Comman.NetworkChangeListener;
import com.example.googlebus.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    AppCompatButton btnSingIn;
    TextView tvSingUp , tvForgetPassword;

    SharedPreferences preferences; // used to store the temp data
    SharedPreferences.Editor editor;

       ProgressDialog progressDialog;

       NetworkChangeListener networkChangeListener = new NetworkChangeListener();

       GoogleSignInOptions googleSignInOptions;
       GoogleSignInClient googleSignInClient;
       AppCompatButton btnSingInWithGoogle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        if (preferences.getBoolean("isLogin", false)) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
        etUserName = findViewById(R.id.etLoginUserName);
        etPassword = findViewById(R.id.etLoginPassword);
        btnSingIn = findViewById(R.id.btnSingIn);
        tvSingUp = findViewById(R.id.tvSingUp);
        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);
        btnSingInWithGoogle = findViewById(R.id.btnSingInWithGoogle);


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);

        btnSingInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().isEmpty()) {
                    etUserName.setError("Please Enter Your Username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Please Enter Your Password");
                } else if (etUserName.getText().toString().length() < 8) {
                    etPassword.setError("please Enter 8 Digit Character Password");
                } else if (!etPassword.getText().toString().matches(".*[a-z].*")) {
                    {
                        etPassword.setError("Password Enter At Least One Lowercase Letter");
                    }
                } else if (!etPassword.getText().toString().matches(".*[A-Z].*")) {
                    {
                        etPassword.setError("Password Enter At Least One Uppercase Letter");
                    }
                } else if (!etPassword.getText().toString().matches(".*[0-9].*")) {
                    {
                        etPassword.setError("Password Enter At Least One Number");
                    }
                } else if (!etPassword.getText().toString().matches(".*[@,$,&,/].*")) {
                    {
                        etPassword.setError("Password Enter at least one special character");
                    }
                } else if
                (!etUserName.getText().toString().matches("[a-zA-Z0-9_]+")) {
                    etUserName.setError("Username must contain only letter, numbers or underscore '_'");
                } else
                {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Login Under Process");
                    progressDialog.show();
                    userLogin();
                }
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ConfirmRegisterMobileNoActivity.class);
                startActivity(intent);

            }
        });

        tvSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 999)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Intent intent = new Intent(LoginActivity.this, MyProfileActivity.class);
                startActivity(intent);
                finish();

            } catch (ApiException e) {
                Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",etUserName.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.loginUserWebService,params,new JsonHttpResponseHandler()

        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                try {
                    String status = response.getString("Success");
                    if (status.equals("1"))

                    {
                        Toast.makeText(LoginActivity.this, "Login Successful Done",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else
                    {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Server error",
                        Toast.LENGTH_SHORT).show();
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