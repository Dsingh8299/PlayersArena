package xyz.toors.toors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {
    private ArrayList<Row_Member> mMemberList;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ValueEventListener seenListener;
    DatabaseReference reference;




    public static class MemberViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView Dp;
        public ImageView unread;
        public TextView MemberName;
        public TextView LastMessage;
        public TextView Time;




        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            Dp = itemView.findViewById(R.id.Dp);
            MemberName = itemView.findViewById(R.id.Member);
            LastMessage = itemView.findViewById(R.id.Last_chat);
            Time = itemView.findViewById(R.id.Time);
            unread=itemView.findViewById(R.id.unread);
        }
    }

    public MembersAdapter(ArrayList<Row_Member> MemberList)
    {

        mMemberList = MemberList;
    }



    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_members,parent,false);
        MemberViewHolder mvh = new MemberViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberViewHolder holder, final int position) {

         final Row_Member currentMember = mMemberList.get(position);
        //holder.Dp.setImageResource(currentMember.getDpResource());
        final String senderId = FirebaseAuth.getInstance().getUid();

        final String senderRoom = senderId+currentMember.getUid();






        Glide.with(holder.Dp).load(currentMember.getDpResource()).placeholder(R.drawable.default_image ).into(holder.Dp);
        holder.MemberName.setText(currentMember.getMemberName());
        reference = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);


                    if(!FirebaseAuth.getInstance().getUid().equals(message.getSenderId())&&message.isIsseen()==false){
                        holder.unread.setVisibility(View.VISIBLE);


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.unread.setVisibility(View.GONE);
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",currentMember.getMemberName());
                intent.putExtra("DpUrl",currentMember.getDpResource());
                intent.putExtra("uid",currentMember.getUid());
                context.startActivity(intent);
            }
        });


        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            holder.LastMessage.setText(lastMsg);
                            String lastmsgtime = snapshot.child("lastMsgTime").getValue(String.class);
                            holder.Time.setText(lastmsgtime);
                            currentMember.setTime(lastmsgtime);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }



    @Override
    public int getItemCount() {
        return mMemberList.size();
    }

    public  void filterList(ArrayList<Row_Member> filteredList)
    {
        mMemberList=filteredList;
        notifyDataSetChanged();
    }


}
