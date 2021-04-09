package xyz.toors.toors;

import android.content.Intent;
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

import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private RecyclerView.LayoutManager postLayoutManager;
    ArrayList<Row_Post> postList;
    private static final int RC_PHOTO_PICKER =  2;
    SharedPreferences sh;
    String name,DpUrl;
    TextView nameText;
    ImageView dpImage;
    private AdView mAdView,madView2;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        search=findViewById(R.id.search_bar_post);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.PostActicityBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.PostActicityBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        DpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        Uri uri = Uri.parse(DpUrl);
        Glide.with(PostActivity.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);
        drawerLayout = findViewById(R.id.drawer_layout);
        postsRecyclerView = findViewById(R.id.post_recyclerView);
        postsRecyclerView.setHasFixedSize(true);
        postLayoutManager = new LinearLayoutManager(this);
        postList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Posts").orderBy("Date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges())
                {
                    String name=documentChange.getDocument().getString("name");
                    String dpUrl=documentChange.getDocument().getString("DpUrl");
                    String des=documentChange.getDocument().getString("Description");
                    String date=documentChange.getDocument().getString("Date");
                    String time=documentChange.getDocument().getString("time");
                    String postImageUrl=documentChange.getDocument().getString("PostImage");
                    String likesReference = documentChange.getDocument().getString("Likes");
                    postList.add(0,new Row_Post(name,dpUrl,postImageUrl,des,date,time,likesReference));


                }
                postAdapter = new PostAdapter(postList);
                postsRecyclerView.setLayoutManager(postLayoutManager);
                postsRecyclerView.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }
        });


    }
    public void search_post(View view)
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

        ArrayList<Row_Post> filteredList = new ArrayList<>();
        for(Row_Post item:postList)
        {
            if(item.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        postAdapter.filterList(filteredList);
    }

    public void goto_post(View v){
        startActivity(new Intent(PostActivity.this,CreatePost.class));
    }
    public void DpChange(View view)
    {
        Toast.makeText(this, "Go to Home to update", Toast.LENGTH_SHORT).show();
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
        recreate();
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