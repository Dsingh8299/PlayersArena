package xyz.toors.toors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
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
    private InterstitialAd mInterstitialAd;
    String URL_RGTR = "https://apptoors.000webhostapp.com/rgtr.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView3 = (TextView) this.findViewById(R.id.textView3);
        textView3.setSelected(true);
        txt_count=(TextView) findViewById(R.id.textView5);
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        LoadData();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/8691691433");
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
            mad.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());
        }
    }
    public void Watch(View view)
    {
        if(mad.isLoaded()){
            mad.show();
        }
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
    }
    @Override
    protected void onResume(){
        mad.resume(this);
        super.onResume();
    }
    @Override
    protected void onDestroy(){
        mad.destroy(this);
        super.onDestroy();
    }

    public void congo(View view) {
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("saveingamename",MODE_PRIVATE);
        final String str = sharedPreferences.getString("ingamename", "");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        if(count>=20) {
                progressDialog.show();
            StringRequest request1 = new StringRequest(Request.Method.POST, URL_RGTR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if (response.equalsIgnoreCase("Registration successfull")) {
                        Intent intent3 = new Intent(Home.this, Congratulations.class);
                        startActivity(intent3);
                        count = count - 20;
                    } else {
                        Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String,String>();
                    params.put("ingamename",str);
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


}