package xyz.toors.toors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity implements RewardedVideoAdListener {
    TextView textView3;
    private RewardedVideoAd mad;
    TextView txt_count;
    private int count=0 ;
    String str;
    private InterstitialAd mInterstitialAd;
    String URL_RGTR = "https://apptoors.000webhostapp.com/freeign.php";
    String URL_RGTR1 = "https://apptoors.000webhostapp.com/pubgign.php";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            progressDialog.show();
            StringRequest request1 = new StringRequest(Request.Method.POST, URL_RGTR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if (response.equalsIgnoreCase("Registration successfull")) {

                        Intent intent3 = new Intent(Home.this, Congratulations.class);
                        startActivity(intent3);

                    } else {
                        Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ingamename", str);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
            requestQueue.add(request1);
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
                    StringRequest request1 = new StringRequest(Request.Method.POST, URL_RGTR1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equalsIgnoreCase("Registration successfull")) {

                                Intent intent3 = new Intent(Home.this, Congratulations.class);
                                startActivity(intent3);

                            } else {
                                Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("ingamename", str);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Home.this);
                    requestQueue.add(request1);
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
}