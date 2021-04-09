package xyz.toors.toors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Feed extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;


    private MessageAdapter mMessageAdapter;
    ArrayList<FriendlyMessage> messages;
    String senderUid;
    int position;
    private RecyclerView GrpRecyclerView;
    private RecyclerView.LayoutManager GrpLayoutManager;

    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private ImageButton mSendButton;
    private static final int RC_PHOTO_PICKER =  2;

    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    DrawerLayout drawerLayout;
    private FirebaseStorage firebaseStorage;
    private StorageReference mChatPhotos;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    String uid;
    SharedPreferences sh;
    String name,DpUrl;
    TextView nameText;
    ImageView dpImage;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        drawerLayout = findViewById(R.id.drawer_layout);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.GroupChatBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        name=sh.getString("name","");
        DpUrl=sh.getString("DpUrl","");
        nameText=findViewById(R.id.MyName);
        dpImage=findViewById(R.id.myDp);
        nameText.setText(name);
        Uri uri = Uri.parse(DpUrl);
        Glide.with(Feed.this).load(uri).placeholder(R.drawable.default_image).into(dpImage);
        senderUid = FirebaseAuth.getInstance().getUid();
        messages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, messages);
        GrpRecyclerView = findViewById(R.id.messageListView);
        GrpRecyclerView.setHasFixedSize(true);
        GrpLayoutManager = new LinearLayoutManager(this);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        if (auth.getCurrentUser() != null) {
            uid = auth.getUid();
            DocumentReference documentReference = fstore.collection("users").document(uid);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    mUsername = documentSnapshot.getString("ingamename").trim();
                    Toast.makeText(Feed.this, "Hello " + mUsername, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Feed.this, LoginActivity.class));
        }




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mChatPhotos = firebaseStorage.getReference().child("chat-photos");


        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);

        mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    FriendlyMessage message = snapshot1.getValue(FriendlyMessage.class);
                    messages.add(message);
                }
                GrpRecyclerView.setLayoutManager(GrpLayoutManager);
                GrpRecyclerView.setAdapter(mMessageAdapter);
                if(GrpRecyclerView.getAdapter().getItemCount()>0){
                    position=GrpRecyclerView.getAdapter().getItemCount()-1;}
                else{
                    position=0;
                }
                GrpRecyclerView.smoothScrollToPosition(position);
                mMessageAdapter.notifyDataSetChanged();
                if (Build.VERSION.SDK_INT >= 11) {
                    GrpRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v,
                                                   int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (bottom < oldBottom) {
                                GrpRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        GrpRecyclerView.smoothScrollToPosition(position);
                                    }
                                }, 100);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null, senderUid);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }

        });
    }







    public void ClickMenu(View view)
    {
        Home.openDrawer(drawerLayout);
    }
    public void Members(View view)
    {
        Toast.makeText(this, "Please Refresh", Toast.LENGTH_SHORT).show();
        Home.redirectActivity(this,Members.class);
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
        recreate();
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