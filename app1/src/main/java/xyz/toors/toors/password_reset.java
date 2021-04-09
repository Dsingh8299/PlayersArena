package xyz.toors.toors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class password_reset extends AppCompatActivity {
    EditText mail;
    FirebaseAuth auth;
    private AdView mAdView,madView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        mail=findViewById(R.id.editTextTextEmailAddress);
        auth= FirebaseAuth.getInstance();
        MobileAds.initialize(this,"ca-app-pub-4669462351447424~6650329449");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.adView2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

    }

    public void sendlink(View view) {
        String email=mail.getText().toString();
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(password_reset.this, "Reset link sent to your Email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(password_reset.this,LoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(password_reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    }
