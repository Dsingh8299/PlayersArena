package xyz.toors.toors;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Aboutus extends AppCompatActivity {
    DrawerLayout drawerLayout;
    SharedPreferences sh;
    String name,DpUrl;
    TextView nameText;
    ImageView dpImage;
    private AdView mAdView,madView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.AboutUsBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.AboutUsBanner2);
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
        Glide.with(Aboutus.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);

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
        recreate();
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
    @Override
    protected void onResume() {
        super.onResume();
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.update("status","online");
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.update("status","offline");

    }


}