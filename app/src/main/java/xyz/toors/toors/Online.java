package xyz.toors.toors;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Online extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private RecyclerView OnlineMembersRecyclerView;
    private RecyclerView.Adapter OnlineMembersAdapter;
    private RecyclerView.LayoutManager OnlineMembersLayoutManager;
    ArrayList<Online_Member> online_members;
    String Ingamename,DpUrl,uid,status;
    SharedPreferences sh;
    String name,dpUrl;
    TextView nameText,oops,onlineRefresh;
    ImageView zeroOnline;
    ImageView dpImage;
    private AdView mAdView,madView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        oops=findViewById(R.id.oops);
        zeroOnline=findViewById(R.id.zero_online);
        onlineRefresh=findViewById(R.id.online_refresh);
        onlineRefresh.setVisibility(View.VISIBLE);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.OnlineBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.OnlineBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        DpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        Uri uri = Uri.parse(DpUrl);
        Glide.with(Online.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        online_members = new ArrayList<>();
        OnlineMembersRecyclerView = findViewById(R.id.online_members);
        OnlineMembersRecyclerView.setHasFixedSize(true);
        OnlineMembersLayoutManager = new LinearLayoutManager(this);


        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DocumentSnapshot documentSnapshot : task.getResult())
                    {
                        Ingamename=documentSnapshot.getString("ingamename");
                        DpUrl=documentSnapshot.getString("DpUrl");
                        uid=documentSnapshot.getString("uid");
                        status=documentSnapshot.getString("status");
                        if("online".equals(status)&&!uid.equals(FirebaseAuth.getInstance().getUid()))
                        {
                            online_members.add(new Online_Member(Ingamename,uid,DpUrl));

                        }
                    }
                    OnlineMembersAdapter = new Online_Members_Adapter(online_members);
                    OnlineMembersRecyclerView.setLayoutManager(OnlineMembersLayoutManager);
                    OnlineMembersRecyclerView.setAdapter(OnlineMembersAdapter);
                    OnlineMembersAdapter.notifyDataSetChanged();
                }
                if(online_members.size()==0)
                {
                    zeroOnline.setVisibility(View.VISIBLE);
                    oops.setVisibility(View.VISIBLE);
                }
                else
                {
                    OnlineMembersRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });






    }
    public void online_refresh(View view)
    {
        finish();
        startActivity(getIntent());
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
    public void OnlineMembers(View v)
    {
        recreate();
    }
    public void DpChange(View view)
    {
        Toast.makeText(this, "Go to Home to update", Toast.LENGTH_SHORT).show();
    }
    public void Post(View v)
    {
        Home.redirectActivity(this, PostActivity.class);
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