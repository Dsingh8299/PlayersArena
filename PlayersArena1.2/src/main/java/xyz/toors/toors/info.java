package xyz.toors.toors;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class info extends AppCompatActivity {

    WebView webView;
    private AdView mAdView,madView2;
    private String url = "https://apptoors.000webhostapp.com/infor.php";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        MobileAds.initialize(this,"ca-app-pub-4669462351447424~6650329449");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.adView2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);
        webView = findViewById(R.id.infor);
        progressBar = (ProgressBar) findViewById(R.id.Progressbar);

        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if(newProgress==100)
                {
                    progressBar.setVisibility(View.GONE );
                }
                super.onProgressChanged(view,newProgress);
            }
        });
    }
}