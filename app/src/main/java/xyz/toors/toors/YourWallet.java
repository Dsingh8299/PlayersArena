package xyz.toors.toors;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class YourWallet extends AppCompatActivity {
    DrawerLayout drawerLayout;
    SharedPreferences sh;
    String name,DpUrl;
    TextView nameText;
    ImageView dpImage;
    SharedPreferences sharedPreferencesForCoins,getCoins;
    SharedPreferences.Editor myCoins;
    int coins=0;
    String showCoins;
    TextView text_coins;
    private RewardedAd mRewardedAd;
    private final String TAG = "Your wallet";
    int realcoins;
    private AdView mAdView,madView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_wallet);
        drawerLayout = findViewById(R.id.drawer_layout);

        text_coins=findViewById(R.id.text_coins);
        sharedPreferencesForCoins = getSharedPreferences("MyCoins", MODE_PRIVATE);
        myCoins = sharedPreferencesForCoins.edit();
        getCoins=getSharedPreferences("MyCoins",MODE_PRIVATE);
        realcoins=+getCoins.getInt("Coins",0);
        showCoins=" "+realcoins+" Coins";
        text_coins.setText(showCoins);



        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        DpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        Uri uri = Uri.parse(DpUrl);
        Glide.with(YourWallet.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.YourWallletBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.YourWallletBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);





    }




    public void ClickMenu(View view)
    {
        Home.openDrawer(drawerLayout);
    }
    public void ClickLogo(View view){
        Home.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view)
    {
        Home.redirectActivity(this,Home.class);
    }
    public void gotoHome(View view){
        Home.redirectActivity(this,Home.class);
    }
    public void YourWallet(View view)
    {
        recreate();
    }
    public void Feed(View view)
    {
        Home.redirectActivity(this,Feed.class);
    }
    public void AboutUs(View view)
    {
        Home.redirectActivity(this,Aboutus.class);
    }
    public void Logout(View view)
    {
        Home.redirectActivity(this,LogOut.class);
    }
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        Home.redirectActivity(this,Members.class);
    }
    public void OnlineMembers(View view){Home.redirectActivity(this,Online.class);}

    public void Post(View v)
    {
        Home.redirectActivity(this, PostActivity.class);
    }
    public void DpChange(View view)
    {
        Toast.makeText(this, "Go to Home to update", Toast.LENGTH_SHORT).show();
    }

    public void WatchandEarn(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-8437463521260836/1986343786",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });





        if (mRewardedAd != null) {
            Activity activityContext = YourWallet.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    realcoins=realcoins+rewardAmount;
                    myCoins.putInt("Coins",realcoins);
                    myCoins.apply();
                    finish();
                    startActivity(getIntent());
                }
            });
        } else {
            Toast.makeText(this, "Try Again in 5 seconds", Toast.LENGTH_SHORT).show();
        }


    }
}