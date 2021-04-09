package xyz.toors.toors;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    SliderView sliderView;
    List<ImageSliderModel>imageSliderModelList;
    private InterstitialAd mInterstitialAd;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSliderModelList = new ArrayList<>();
        sliderView = findViewById(R.id.imageSlider);
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl1));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl2));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl3));
        sliderView.setSliderAdapter(new ImageSliderAdapter( this,imageSliderModelList));
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/8691691433");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

    }
    public void signup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    public void lgin(View view){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        else
        {
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
        }
    }
}