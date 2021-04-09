package xyz.toors.toors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class info1 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    WebView webView;
    private AdView mAdView,madView2;
    private String url = "https://apptoors.000webhostapp.com/infor1.php";
    ProgressBar progressBar;
    ImageButton backhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info1);
        backhome=findViewById(R.id.back_home);
        backhome.setVisibility(View.VISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.info1Banner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.info1Banner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        CookieManager.getInstance().setAcceptCookie(true);

        webView = findViewById(R.id.infor);
        progressBar = (ProgressBar) findViewById(R.id.Progressbar);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                progressBar.setVisibility(ProgressBar.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        });
    }

    public void back_home(View view)
    {
        startActivity(new Intent(info1.this,Home.class));
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
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        Home.redirectActivity(this,Members.class);
    }
    public void gotoHome(View view){
        Home.redirectActivity(this,Home.class);
    }
    public void YourWallet(View view)
    {
        Home.redirectActivity(this,YourWallet.class);
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
    public void OnlineMembers(View view){Home.redirectActivity(this,Online.class);}

    public void Post(View v)
    {
        Home.redirectActivity(this, PostActivity.class);
    }
    public void DpChange(View view)
    {
        Toast.makeText(this, "Go to Home to update", Toast.LENGTH_SHORT).show();
    }

}