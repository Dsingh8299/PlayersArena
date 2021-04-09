package xyz.toors.toors;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.Comparator;

public class Members extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private RecyclerView MembersRecyclerView;
    private MembersAdapter mMembersAdapter;
    private RecyclerView.LayoutManager MembersLayoutManager;
    FirebaseFirestore fstore;
    String Ingamename;
    String DpUrl,uid;
    ArrayList<Row_Member> row_members;
    FirebaseAuth auth;
    String myUid;
    long timestamp=0;
    EditText search;
    SharedPreferences sh;
    String name,dpUrl;
    TextView nameText;
    ImageView dpImage;
    private AdView mAdView,madView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.MembersBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.MembersBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        dpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        if(DpUrl!=null){
        Uri uri = Uri.parse(DpUrl);
        Glide.with(Members.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);}
        fstore = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        auth=FirebaseAuth.getInstance();
        myUid=auth.getUid();
        row_members=new ArrayList<>();
        MembersRecyclerView = findViewById(R.id.members_rview);
        MembersRecyclerView.setHasFixedSize(true);
        search=findViewById(R.id.search_bar);



        MembersLayoutManager = new LinearLayoutManager(this);


        fstore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (DocumentSnapshot documentSnapshot : task.getResult())
                    {
                        Ingamename = documentSnapshot.getString("ingamename");
                        DpUrl = documentSnapshot.getString("DpUrl");
                        uid = documentSnapshot.getString("uid");
                        if(myUid.equals(uid)) {
                            continue;
                        }
                        row_members.add(new Row_Member(uid, DpUrl, Ingamename, "Tap to chat"));
                    }
                    mMembersAdapter = new MembersAdapter(row_members);
                    MembersRecyclerView.setLayoutManager(MembersLayoutManager);
                    MembersRecyclerView.setAdapter(mMembersAdapter);
                    mMembersAdapter.notifyDataSetChanged();
                }
            }
        });


   }


    public void ClickMenu1(View view)
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
        Home.redirectActivity(this,LogOut.class);
    }
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        recreate();
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
    public void search(View view)
    {
        search.setVisibility(View.VISIBLE);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

    }

    private void filter(String text) {
        ArrayList<Row_Member> filteredList = new ArrayList<>();
        for(Row_Member item:row_members)
        {
            if(item.getMemberName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        mMembersAdapter.filterList(filteredList);
    }

    public void refresh(View view) {

        Collections.sort(row_members, new Comparator<Row_Member>() {
            @Override
            public int compare(Row_Member o1, Row_Member o2) {

                return o2.getTime().compareTo(o1.getTime());
            }
        });
        mMembersAdapter = new MembersAdapter(row_members);
        MembersRecyclerView.setLayoutManager(MembersLayoutManager);
        MembersRecyclerView.setAdapter(mMembersAdapter);
        mMembersAdapter.notifyDataSetChanged();
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