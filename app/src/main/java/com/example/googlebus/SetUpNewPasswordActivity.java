package com.example.googlebus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googlebus.Comman.NetworkChangeListener;
import com.example.googlebus.Comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetUpNewPasswordActivity extends AppCompatActivity {

    String strMobileno;
    EditText etNewPassword, etConfirmPassword;
    AppCompatButton btnSetUpPassword;

    ProgressDialog progressDialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_password);


        etNewPassword = findViewById(R.id.etSetUpNewPassword);
        etConfirmPassword = findViewById(R.id.etSetUpNewPasswordConfirmPassword);
        btnSetUpPassword = findViewById(R.id.btnSetUpPassword);

        strMobileno = getIntent().getStringExtra("mobile");
        Toast.makeText(this,strMobileno,Toast.LENGTH_SHORT).show();


        btnSetUpPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNewPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(SetUpNewPasswordActivity.this, "Please Enter New or Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    etConfirmPassword.setError("Password Did Not Match");
                }
                else
                {
                    progressDialog = new ProgressDialog(SetUpNewPasswordActivity.this);
                    progressDialog.setTitle("Updating Password");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    forgetPassword();
                }

            }
        });
    }

    private void forgetPassword() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("mobileno",strMobileno);
        params.put("password",etNewPassword.getText().toString());

        client.post(Urls.forgetPasswordWebService,
                params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("Success");
                            if (status.equals("1"))
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "New Password Set up successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SetUpNewPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                            } else
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "Password Not Changed",
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
                        Toast.makeText(SetUpNewPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
