package com.example.ratha.chatapp_socketio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ratha.chatapp_socketio.Message;
import com.example.ratha.chatapp_socketio.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ratha on 12/21/2017.
 */

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    List<Message> messages;
    Context context;

    public MessageAdapter(Context context ,List<Message> messages){
        this.context=context;
        this.messages=messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void setMessage( Message message){
        if(null!=message){
            this.messages.add(message);
            notifyItemInserted(this.messages.size()-1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout=-1;
        switch (viewType){
            case Message.TYPE_MESSAGE:
                layout=R.layout.message_item_recycler_view;break;
            case Message.TYPE_ACTION:
                layout=R.layout.join_item_recycler_view; break;
            case Message.TYPE_LOG:
                layout=R.layout.log_item_recycler_view; break;
        }

        View view= LayoutInflater.from(context).inflate(layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("messages->","size "+this.messages.size());
        if(this.messages!=null){
            Message message=this.messages.get(position);
            if(!TextUtils.isEmpty(message.getUsername()))
                holder.tvProfile.setText(message.getUsername());
            else
                holder.tvProfile.setText("AYA");

            if(!TextUtils.isEmpty(message.getMessage()))
                holder.tvMessage.setText(message.getMessage());
            else
                holder.tvMessage.setText("");

        }else
            Toast.makeText(context, "Message list object os null!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvProfile)
        TextView tvProfile;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
