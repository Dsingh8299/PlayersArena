package xyz.toors.toors;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.toors.toors.Notification.Client;
import xyz.toors.toors.Notification.Data;
import xyz.toors.toors.Notification.MyResponse;
import xyz.toors.toors.Notification.Sender;

public class ChatActivity extends AppCompatActivity {
    CircleImageView DpChat;
    TextView ChatName;
    int position;
    ChatAdapter chatAdapter;
    ArrayList<Message> messages;
    String senderRoom,receiverRomm;
    EditText Chatmsg;
    FirebaseDatabase database;
    String senderUid;
    String receiverUid;
    private RecyclerView MembersRecyclerView;
    private RecyclerView.LayoutManager MembersLayoutManager;
    ValueEventListener seenListener;
    DatabaseReference reference;
    APIService apiService;
    private AdView mAdView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.ChatBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Chatmsg=findViewById(R.id.ChatMsg);
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this,messages);

        MembersRecyclerView = findViewById(R.id.recyclerViewChat);
        MembersRecyclerView.setHasFixedSize(true);
        MembersLayoutManager = new LinearLayoutManager(this);
        DpChat=findViewById(R.id.DpChat);
        ChatName=findViewById(R.id.ChatName);
        database = FirebaseDatabase.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


         name=getIntent().getStringExtra("name");
        String DpUrl=getIntent().getStringExtra("DpUrl");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid+receiverUid;
        receiverRomm = receiverUid+senderUid;
        database.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                MembersRecyclerView.setLayoutManager(MembersLayoutManager);
                MembersRecyclerView.setAdapter(chatAdapter);
                if(MembersRecyclerView.getAdapter().getItemCount()>0){
                position=MembersRecyclerView.getAdapter().getItemCount()-1;}
                else{
                    position=0;
                }
                MembersRecyclerView.smoothScrollToPosition(position);
                chatAdapter.notifyDataSetChanged();
                if (Build.VERSION.SDK_INT >= 11) {
                    MembersRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v,
                                                   int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (bottom < oldBottom) {
                                MembersRecyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MembersRecyclerView.smoothScrollToPosition(position);
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
        seenMessage(receiverUid);



        Glide.with(ChatActivity.this).load(DpUrl).placeholder(R.drawable.default_image).into(DpChat);
        ChatName.setText(name);



    }

    private void seenMessage(String userid)
    {
        reference = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    HashMap<String,Object> lastMsgObj =new HashMap<>();
                    lastMsgObj.put("isseen",true);
                    dataSnapshot.getRef().updateChildren(lastMsgObj);
                    message.setIsseen(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendmsgbtn(View view) {

        String messageTxt = Chatmsg.getText().toString();
        Chatmsg.setText("");
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        Date date=new Date();

        final Message message = new Message(messageTxt,senderUid,currentTime);

        HashMap<String,Object> lastMsgObj = new HashMap<>();
        lastMsgObj.put("lastMsg",message.getMessage());
        lastMsgObj.put("lastMsgTime",currentTime);
        lastMsgObj.put("timestamp",date.getTime());
        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
        database.getReference().child("chats").child(receiverRomm).updateChildren(lastMsgObj);


        database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {



                database.getReference().child("chats").child(receiverRomm).child("messages").push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });



            }
        });
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiverUid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usertoken=dataSnapshot.getValue(String.class);
                sendNotifications(usertoken, "PlayersArena","You may have a new Message ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    public void currentUser(String uid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
        editor.putString("currentUser",uid);
        editor.apply();
    }
    public void sendNotifications(String receiver, final String title, String msg)
    {
        Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),title, msg);
        Sender sender = new Sender(data,receiver);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200)
                {
                    if(response.body().success!=1)
                    {
                        Toast.makeText(ChatActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(receiverUid);
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.update("status","online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentUser("none");
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid());
        reference.update("status","offline");

    }


}