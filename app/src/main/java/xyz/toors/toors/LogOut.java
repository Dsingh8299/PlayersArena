package xyz.toors.toors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    SharedPreferences sh;
    String name,DpUrl;
    TextView nameText;
    ImageView dpImage;
    private AdView mAdView,madView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.LogoutBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.LogoutBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        drawerLayout = findViewById(R.id.drawer_layout);
        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        DpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        Uri uri = Uri.parse(DpUrl);
        Glide.with(LogOut.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);
        auth=FirebaseAuth.getInstance();
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
       recreate();
    }
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        Home.redirectActivity(this,Members.class);
    }

    public void btn_logout(View view) {
        auth.signOut();
        Toast.makeText(this, "You are successfully logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LogOut.this,LoginActivity.class));
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

    public void stay_here(View view) {
        startActivity(new Intent(LogOut.this,Home.class));
    }
}