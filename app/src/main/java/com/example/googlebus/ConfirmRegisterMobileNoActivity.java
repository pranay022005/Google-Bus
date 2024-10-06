package com.example.googlebus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googlebus.Comman.NetworkChangeListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmRegisterMobileNoActivity extends AppCompatActivity {

    EditText etConfirmRegisterMobileNo;
    View btnVerify;
    ProgressDialog progressDialog;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_register_mobile_no);

        etConfirmRegisterMobileNo = findViewById(R.id.etConfirmRegisterMobileNo);
        btnVerify = findViewById(R.id.btnConfirmRegisterMobileNoVerify);

      btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmRegisterMobileNo.getText().toString().isEmpty()) {
                    etConfirmRegisterMobileNo.setError("Please Enter your Mobile Number");
                } else if (etConfirmRegisterMobileNo.getText().toString().length() != 10) {
                    etConfirmRegisterMobileNo.setError("Invalid Mobile No");
                } else {
                    progressDialog = new ProgressDialog(ConfirmRegisterMobileNoActivity.this);
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.setMessage("Registration Verification is process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + etConfirmRegisterMobileNo.getText().toString(),
                                60, TimeUnit.SECONDS, ConfirmRegisterMobileNoActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ConfirmRegisterMobileNoActivity.this, "Verification Completed", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ConfirmRegisterMobileNoActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationCode, @NonNull
                                    PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        Intent intent = new Intent(ConfirmRegisterMobileNoActivity.this, ForgetPasswordVerifyOTPActivity.class);
                                        intent.putExtra("verificationCode", verificationCode);
                                        intent.putExtra("mobileno", etConfirmRegisterMobileNo.getText().toString());
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
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
