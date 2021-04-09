package xyz.toors.toors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<FriendlyMessage> friendlyMessages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;
    public MessageAdapter(Context context, ArrayList<FriendlyMessage> friendlyMessages){
        this.context=context;
        this.friendlyMessages=friendlyMessages;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.group_item_sent,parent,false);
            return new MessageAdapter.SentViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.group_item_receive,parent,false);
            return new MessageAdapter.ReceiverViewHolder(view);
        }

    }
    @Override
    public int getItemViewType(int position) {
        FriendlyMessage message = friendlyMessages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId()))
        {

            return ITEM_SENT;
        }
        else
        {
            return ITEM_RECEIVE;
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendlyMessage message = friendlyMessages.get(position);
        if(holder.getClass()== MessageAdapter.SentViewHolder.class)
        {
            MessageAdapter.SentViewHolder viewHolder = (MessageAdapter.SentViewHolder)holder;
            viewHolder.group_item_send.setText(message.getText());
            viewHolder.sender.setText(message.getName());
        }
        else
        {
            MessageAdapter.ReceiverViewHolder viewHolder = (MessageAdapter.ReceiverViewHolder)holder;
            viewHolder.group_item_recieve.setText(message.getText());
            viewHolder.receiver.setText(message.getName());
        }

    }
    @Override
    public int getItemCount() {
        return friendlyMessages.size();
    }
    public class SentViewHolder extends RecyclerView.ViewHolder{
        TextView group_item_send,sender;

        public SentViewHolder(@NonNull View itemView) {

            super(itemView);
            group_item_send=itemView.findViewById(R.id.group_sentmsg);
            sender=itemView.findViewById(R.id.sender);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView group_item_recieve,receiver;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            group_item_recieve=itemView.findViewById(R.id.group_receivedmsg);
            receiver=itemView.findViewById(R.id.receiver);
        }
    }

}
