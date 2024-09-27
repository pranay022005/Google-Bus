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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googlebus.Comman.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Account_Profile_Activity extends AppCompatActivity {

    ImageView ivProfilePhoto;
    TextView tvName, tvMobileNo, tvEmailId, tvUsername;
    AppCompatButton btnEditProfile, btnSignOut;

    SharedPreferences preferences;
    String strUsername;

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(Account_Profile_Activity.this);

        strUsername = preferences.getString("username", "");

        ivProfilePhoto = findViewById(R.id.ivMyProfileProfilePhoto);
        btnEditProfile = findViewById(R.id.btnMyProfileEditeProfile);
        tvName = findViewById(R.id.tvMyProfileName);
        tvMobileNo = findViewById(R.id.tvMyProfileMobileNo);
        tvEmailId = findViewById(R.id.tvMyProfileEmailId);
        tvUsername = findViewById(R.id.tvMyProfileUsername);
        btnSignOut = findViewById(R.id.btnMyProfileSingOut);

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
        AsyncHttpClient client = new AsyncHttpClient(); //client Server communication //over network data pass
        RequestParams params = new RequestParams(); //put the data AsyncHttpClient object

        params.put("username",strUsername);

        client.post(Urls.myDetailsWebService,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = response.getJSONArray("getMyDetails");

                            for(int i=0; i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String strid = jsonObject.getString("id");
                                String strname = jsonObject.getString("name");
                                String strmobileno = jsonObject.getString("mobile_no");
                                String stremailid = jsonObject.getString("email_id");
                                String strusername = jsonObject.getString("username");

                                tvName.setText(strname);
                                tvMobileNo.setText(strmobileno);
                                tvEmailId.setText(stremailid);
                                tvUsername.setText(strusername);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(Account_Profile_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

