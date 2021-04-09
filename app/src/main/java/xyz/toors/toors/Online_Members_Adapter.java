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

import java.util.ArrayList;

public class Online_Members_Adapter extends RecyclerView.Adapter<Online_Members_Adapter.Online_Members_ViewHolder> {
    private ArrayList<Online_Member> online_members;
    Context context;


    public static class Online_Members_ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView dp;
        public Online_Members_ViewHolder(@NonNull View itemView) {

            super(itemView);
            Name=itemView.findViewById(R.id.Name);
            dp=itemView.findViewById(R.id.DpOnline);
        }
    }
    public Online_Members_Adapter(ArrayList<Online_Member> online_members)
    {
        this.online_members=online_members;
    }

    @NonNull
    @Override
    public Online_Members_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_row_member,parent,false);
        Online_Members_ViewHolder mvh = new Online_Members_ViewHolder(v);
        return mvh;

    }

    @Override
    public void onBindViewHolder(@NonNull Online_Members_ViewHolder holder, int position) {
        final Online_Member currentOnline = online_members.get(position);
        holder.Name.setText(currentOnline.getName());
        Glide.with(holder.dp).load(currentOnline.getDpResource()).placeholder(R.drawable.default_image ).into(holder.dp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",currentOnline.getName());
                intent.putExtra("DpUrl",currentOnline.getDpResource());
                intent.putExtra("uid",currentOnline.getUid());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return online_members.size();
    }
}
