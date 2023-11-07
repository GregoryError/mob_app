package com.mob.mobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mob.mobapp.R;
import com.mob.mobapp.pojos.Message;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final Context context;
    private ArrayList<Message> messageArrayList;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        holder.textViewDateTime.setText(message.getDateTime());
        holder.textViewComment.setText(message.getContent());
        if (message.getFromUser()) {
            holder.textViewFullName.setText("Вы:");
            holder.imageViewAvatar.setBackgroundResource(R.drawable.user_24);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.cardViewChatItem.getLayoutParams();
            params.rightMargin = 120;
            holder.cardViewChatItem.setLayoutParams(params);
        } else {
            holder.imageViewAvatar.setBackgroundResource(R.drawable.master_24);
            holder.textViewFullName.setText("Мастер:");
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.cardViewChatItem.getLayoutParams();
            params.leftMargin = 120;
            holder.cardViewChatItem.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFullName;
        public TextView textViewComment;
        public TextView textViewDateTime;
        public ImageView imageViewAvatar;

        public CardView cardViewChatItem;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            cardViewChatItem = itemView.findViewById(R.id.cardViewChatItem);
        }
    }
}
