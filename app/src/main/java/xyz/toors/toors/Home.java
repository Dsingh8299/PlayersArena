package xyz.toors.toors;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.toors.toors.Notification.Token;

public class Home extends AppCompatActivity {
    DrawerLayout drawerLayout;

    private StorageReference Dp;
    private FirebaseStorage firebaseStorage;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    String uid,mUsername;
    CircleImageView DpImage;
    TextView Myname;
    String myName,dpUrlShared;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    SharedPreferences sharedPreferencesForCoins,getCoins;
    SharedPreferences.Editor myCoins;
    private InterstitialAd mInterstitialAd;
    SharedPreferences sh;



    private static final int RC_PHOTO_PICKER =  2;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        loadAd();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.HomeBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferencesForCoins = getSharedPreferences("MyCoins", MODE_PRIVATE);
        myCoins = sharedPreferencesForCoins.edit();

        drawerLayout = findViewById(R.id.drawer_layout);
        firebaseStorage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        Dp=firebaseStorage.getReference().child("display-picture");
        uid=auth.getUid();

        DocumentReference documentReference = fstore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mUsername = documentSnapshot.getString("ingamename").trim();
            }
        });

        DpImage=findViewById(R.id.myDp);
        Myname=findViewById(R.id.MyName);
        FirebaseMessaging.getInstance().subscribeToTopic(auth.getUid());
        setName();
        updateToken(FirebaseInstanceId.getInstance().getToken());
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         myEdit = sharedPreferences.edit();

        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        dpUrlShared=sh.getString("DpUrl","");
        Uri uri = Uri.parse(dpUrlShared);
        Glide.with(Home.this).load(uri).placeholder(R.drawable.default_image).into(DpImage);



    }
    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
    public  void setName()
    {
        DocumentReference documentReference = fstore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                myName = documentSnapshot.getString("ingamename").trim();
               Myname.setText(myName);
               myEdit.putString("name",myName);
               myEdit.apply();
            }
        });
    }
    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }


    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }
    public void Post(View v)
    {
        startActivity(new Intent(Home.this,PostActivity.class));
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void DpChange(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }
    public void OnlineMembers(View v)
    {
        startActivity(new Intent(Home.this,Online.class));
    }



    public void ClickHome(View view)
    {
        recreate();
    }
    public void gotoHome(View view)
    {
        recreate();
    }
    public void YourWallet(View view)
    {
        redirectActivity(this,YourWallet.class);
    }

    public void Feed(View view)
    {
        redirectActivity(this,Feed.class);
    }
    public void AboutUs(View view)
    {
        redirectActivity(this,Aboutus.class);
    }
    public void Logout(View view)
    {
        redirectActivity(this,LogOut.class);
    }
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        redirectActivity(this,Members.class);
    }
    public static void redirectActivity(Activity activity,Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    public void updateDp(Uri uriiya)
        {
            final StorageReference photoRef=Dp.child(uriiya.getLastPathSegment());
            photoRef.putFile(uriiya).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri DpUrl = uri;
                            String fDpUrl=DpUrl.toString();
                            DocumentReference documentReference=fstore.collection("users").document(uid);
                            documentReference.update("DpUrl",fDpUrl);

                            myEdit.putString("DpUrl",fDpUrl);
                            myEdit.apply();

                            Glide.with(Home.this).load(DpUrl).placeholder(R.drawable.default_image).into(DpImage);
                        }
                    });

                }
            });
        }



   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_PHOTO_PICKER&&resultCode==RESULT_OK)
        {
            final Uri selectedImageUri = data.getData();
            updateDp(selectedImageUri);

        }
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
        closeDrawer(drawerLayout);
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.update("status","offline");

    }

    public void info(View view) {
        startActivity(new Intent(Home.this,info.class));
    }

    public void Winner(View view) {
        startActivity(new Intent(Home.this,Winner.class));

    }

    public void info1(View view) {
        startActivity(new Intent(Home.this,info1.class));

    }

    public void Winner1(View view) {
        startActivity(new Intent(Home.this,Winner1.class));

    }

    public void congo1(View view) {
        if(mInterstitialAd!=null)
        {
            mInterstitialAd.show(this);
        }
        else {
            getCoins = getSharedPreferences("MyCoins", MODE_PRIVATE);


            int realCoins = +getCoins.getInt("Coins", 0);
            if (realCoins >= 5) {

                DocumentReference documentReference=fstore.collection("Registered Users pubg").document(FirebaseAuth.getInstance().getUid());

                final Map<String, String> params = new HashMap<>();
                params.put("ingamename", mUsername);
                documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if(task1.isSuccessful())
                        {
                            Toast.makeText(Home.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Home.this,Congratulations.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Home.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myCoins.putInt("Coins", 0);
                myCoins.apply();
            } else {
                Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void congo(View view) {

            getCoins = getSharedPreferences("MyCoins", MODE_PRIVATE);


            int realCoins = +getCoins.getInt("Coins", 0);
            if (realCoins >= 5) {
                DocumentReference documentReference=fstore.collection("Registered Users freefire").document(FirebaseAuth.getInstance().getUid());

                final Map<String, String> params = new HashMap<>();
                params.put("ingamename", mUsername);
                documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if(task1.isSuccessful())
                        {
                            Toast.makeText(Home.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Home.this,Congratulations.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Home.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myCoins.putInt("Coins", 0);
                myCoins.apply();
            } else {
                Toast.makeText(this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
            }
        }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-8437463521260836/6940762341",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Home.this.mInterstitialAd = interstitialAd;

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        getCoins=getSharedPreferences("MyCoins",MODE_PRIVATE);


                                        int realCoins=+getCoins.getInt("Coins",0);
                                        if(realCoins>=5)
                                        {
                                            DocumentReference documentReference=fstore.collection("Registered Users pubg").document(FirebaseAuth.getInstance().getUid());

                                            final Map<String, String> params = new HashMap<>();
                                            params.put("ingamename", mUsername);
                                            documentReference.set(params).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task1) {
                                                    if(task1.isSuccessful())
                                                    {
                                                        Toast.makeText(Home.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent =new Intent(Home.this,Congratulations.class);
                                                        startActivity(intent);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Home.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            myCoins.putInt("Coins",0);
                                            myCoins.apply();
                                        }
                                        else
                                        {
                                            Toast.makeText(Home.this, "Not Enough Coins", Toast.LENGTH_SHORT).show();
                                        }
                                        Home.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Home.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;


                    }
                });
    }

    public void RoomIdPass(View view) {

        fstore.collection("Registered Users pubg").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {


                        startActivity(new Intent(Home.this, RoomIdPassActivity.class));

                } else {
                    Toast.makeText(Home.this, "Not Registered", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}