package xyz.toors.toors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements RewardedVideoAdListener {
    TextView textView3;
    private RewardedVideoAd mad;
    TextView txt_count;
    private int count=0 ;
    String str;
    FirebaseFirestore fstore;
    private InterstitialAd mInterstitialAd,mInterstitialAd1;
    String URL_RGTR = "https://apptoors.000webhostapp.com/freeign.php";
    String URL_RGTR1 = "https://apptoors.000webhostapp.com/pubgign.php";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fstore=FirebaseFirestore.getInstance();
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        textView3 = (TextView) this.findViewById(R.id.textView3);
        textView3.setSelected(true);
        txt_count=(TextView) findViewById(R.id.textView5);
        MobileAds.initialize(this,"ca-app-pub-4669462351447424~6650329449");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        LoadData();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4669462351447424/9365810786");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId("ca-app-pub-4669462351447424/2810088832");
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }
    private void loadRewardedVideoAd(){
        if(!mad.isLoaded()){
            mad.loadAd(
                            "ca-app-pub-4669462351447424/9907634902",
                    new AdRequest.Builder().build());
        }
    }
    public void Watch(View view)
    {
        if (!mad.isLoaded()) {
            Toast.makeText(this, "Try Again in 5 sec...", Toast.LENGTH_SHORT).show();
        }
        mad.show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        count++;
        txt_count.setText(String.valueOf(count));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoCompleted() {
        loadRewardedVideoAd();
    }
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("savecount",MODE_PRIVATE);
        SharedPreferences.Editor  editor = sharedPreferences.edit();
        editor.putInt("countvalue",count);
        editor.apply();
    }
    public void LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("savecount",MODE_PRIVATE);
        count = sharedPreferences.getInt("countvalue",MODE_PRIVATE);
        txt_count.setText(String.valueOf(count));
    }
    @Override
    protected void onPause(){
        mad.pause(this);
        super.onPause();
        saveData();
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        str = sharedPreferences.getString("ingamename", "");
    }
    @Override
    protected void onResume(){
        mad.resume(this);
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
         str = sharedPreferences.getString("ingamename", "");
    }
    @Override
    protected void onDestroy(){
        mad.destroy(this);
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        str = sharedPreferences.getString("ingamename", "");
    }


    public void congo(View view) {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        str = sharedPreferences.getString("ingamename", "");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        if(count>=5) {
            count=count-5;
            if (str.equals("")) {
                Toast.makeText(this, "Signup Again Please", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home.this, SignupActivity.class);
                startActivity(intent);
            } else {
                count = count - 5;
                progressDialog.show();

                DocumentReference documentReference=fstore.collection("Registered Users free fire").document(str);

                final Map<String, String> params = new HashMap<>();
                params.put("ingamename", str);
                documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        progressDialog.dismiss();
                        if(task1.isSuccessful())
                        {
                            Toast.makeText(Home.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Home.this,Congratulations.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Home.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }








        }
        else
            {
            Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
        }
    }
    public void info(View view)
    {
        Intent intent=new Intent(Home.this,info.class);
        startActivity(intent);
    }

    public void Winner(View view) {
        Intent intent=new Intent(Home.this,Winner.class);
        startActivity(intent);
    }


    public void congo1(View view) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename", MODE_PRIVATE);
        str = sharedPreferences.getString("ingamename", "");

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            if (count >= 5) {
                if (str.equals("")) {
                    Toast.makeText(this, "Signup Again Please", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Home.this, SignupActivity.class);
                    startActivity(intent);
                } else {
                    count = count - 5;
                    progressDialog.show();

                    DocumentReference documentReference=fstore.collection("Registered Users").document(str);

                    final Map<String, String> params = new HashMap<>();
                    params.put("ingamename", str);
                    documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task1) {
                            progressDialog.dismiss();
                            if(task1.isSuccessful())
                            {
                                Toast.makeText(Home.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(Home.this,Congratulations.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(Home.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }else {
                Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
            }
        }

    public void info1(View view) {
        Intent intent=new Intent(Home.this,info1.class);
        startActivity(intent);
    }

    public void Winner1(View view) {
        Intent intent=new Intent(Home.this,Winner1.class);
        startActivity(intent);
    }


    public void RoomIdPass(View view) {
        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                Intent intent1 = new Intent(Home.this, RoomIdPassActivity.class);
                startActivity(intent1);
                mInterstitialAd1.loadAd(new AdRequest.Builder().build());
            }

        });

        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        str = sharedPreferences.getString("ingamename", "");
        if(str!="") {
            fstore.collection("Registered Users").document(str).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        if(mInterstitialAd1.isLoaded()){
                            mInterstitialAd1.show();
                        }
                        else {
                            startActivity(new Intent(Home.this, RoomIdPassActivity.class));
                        }
                    } else {
                        Toast.makeText(Home.this, "Not Registered", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Sign-up first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Home.this,SignupActivity.class));
        }

    }
}