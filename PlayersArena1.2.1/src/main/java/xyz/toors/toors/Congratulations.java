package xyz.toors.toors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class Congratulations extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private AdView mAdView,madView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        MobileAds.initialize(this,
                "ca-app-pub-4669462351447424~6650329449");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.adView2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4669462351447424/5809709154");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent1 = new Intent(Congratulations.this, Home.class);
                startActivity(intent1);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }

    public void backhome(View view) {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();

        }
        else {
            Intent intent3 = new Intent(Congratulations.this, Home.class);
            startActivity(intent3);
        }
    }
}