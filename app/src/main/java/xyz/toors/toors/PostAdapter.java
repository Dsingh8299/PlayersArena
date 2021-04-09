package xyz.toors.toors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<Row_Post> postsList;
    Context context;
    boolean likeChecker=false;
    String likesRef;


    public static class PostViewHolder extends RecyclerView.ViewHolder{
        CircleImageView dp;
        TextView name;
        TextView time;
        TextView date;
        TextView des;
        ImageView postImage;
        ImageButton likesVBtn;
        TextView likesCount;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            dp= itemView.findViewById(R.id.post_dp);
            name=itemView.findViewById(R.id.post_name);
            time= itemView.findViewById(R.id.post_time);
            date=itemView.findViewById(R.id.post_date);
            des= itemView.findViewById(R.id.post_des);
            postImage=itemView.findViewById(R.id.post_image);
            likesVBtn= itemView.findViewById(R.id.likes_btn);
            likesCount=itemView.findViewById(R.id.likes_count);
        }
    }

    public PostAdapter( ArrayList<Row_Post> postsList)
    {
        this.postsList=postsList;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post,parent,false);
        PostViewHolder pvh =new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final Row_Post currentPost = postsList.get(position);
        holder.name.setText(currentPost.getName());
        holder.time.setText(currentPost.getTime());
        holder.date.setText(currentPost.getDate());
        holder.des.setText(currentPost.getDescription());
        likesRef=currentPost.getLikesReference();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Likes").child(likesRef);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                int count=(int)snapshot.getChildrenCount();
                holder.likesCount.setText(""+count+"  likes");
                        if(snapshot.hasChild(FirebaseAuth.getInstance().getUid()))
                    holder.likesVBtn.setColorFilter(R.drawable.googleg_disabled_color_18);


                }          }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Glide.with(holder.postImage).load(currentPost.getImageUrl()).into(holder.postImage);
        if(currentPost.getDpUrl()!=null)
        {
            Glide.with(holder.dp).load(currentPost.getDpUrl()).placeholder(R.drawable.default_image).into(holder.dp);
        }
        holder.likesVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeChecker=true;
                currentPost.setLiked(true);
                DatabaseReference likesReference = FirebaseDatabase.getInstance().getReference().child("Likes").child(currentPost.getLikesReference());
                likesReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        likesReference.child(FirebaseAuth.getInstance().getUid()).setValue(true);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });




    }


    @Override
    public int getItemCount() {
        if(postsList.size()!=0)
        return postsList.size();
        else
            return 0;
    }

    public  void filterList(ArrayList<Row_Post> filteredList)
    {
        postsList=filteredList;
        notifyDataSetChanged();
    }
}
