package xyz.toors.toors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreatePost extends AppCompatActivity {
    private static final int RC_PHOTO_PICKER = 2;
    DrawerLayout drawerLayout;
    Uri postImageUri;
    ImageButton postImageButton;
    EditText description;
    private StorageReference postsImage;
    String firebasePostImageUrl;
    FirebaseDatabase postsDatabase;
    FirebaseFirestore fStore;
    String name,dpUrl;
    private AdView mAdView,madView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        drawerLayout = findViewById(R.id.drawer_layout);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.CreatePostBanner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        madView2 = findViewById(R.id.CreatePostBanner2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        madView2.loadAd(adRequest1);

        postImageButton=findViewById(R.id.post_image);
        description=findViewById(R.id.description);
        postsDatabase=FirebaseDatabase.getInstance();
        fStore=FirebaseFirestore.getInstance();
        postsImage= FirebaseStorage.getInstance().getReference().child("Posts");
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    name=documentSnapshot.getString("ingamename");
                    dpUrl=documentSnapshot.getString("DpUrl");


                }
            }
        });
    }

    public void select_post_image(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_PHOTO_PICKER&&resultCode==RESULT_OK)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            postImageUri=data.getData();
            final StorageReference photoRef = postsImage.child(postImageUri.getLastPathSegment());
            photoRef.putFile(postImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                       final Uri postUri=uri;
                       firebasePostImageUrl=uri.toString();
                       progressDialog.dismiss();
                       postImageButton.setImageURI(postImageUri);
                        }
                    });
                }
            });



        }
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
        Home.redirectActivity(this,Online.class);
    }
    public void Post(View v)
    {
        Home.redirectActivity(this,PostActivity.class);
    }

    public void create_post(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String des=description.getText().toString();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String key=FirebaseAuth.getInstance().getUid()+formattedDate+currentTime;


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Posts").document(key);


        final Map<String, String> params = new HashMap<>();
        params.put("name",name);
        params.put("DpUrl",dpUrl);
        params.put("Description",des);
        params.put("Date",formattedDate);
        params.put("time",currentTime);
        params.put("PostImage",firebasePostImageUrl);
        params.put("Likes",key);
        documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task1) {
                if(task1.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePost.this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreatePost.this,PostActivity.class));
                }
                else
                {
                    Toast.makeText(CreatePost.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}