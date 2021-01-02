package com.example.schedulemessenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemessenger.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private List<Message> messageList = new ArrayList<>();

    public MessageAdapter(ArrayList<Message> models) {
        messageList = models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message currentMessage = messageList.get(position);
        holder.phoneNumber.setText(currentMessage.getPhoneNumber());
        holder.messageStatus.setText(currentMessage.getMessageStatus());
        holder.messageText.setText(currentMessage.getMessageText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        public TextView phoneNumber;
        public TextView messageText;
        public TextView messageStatus;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phone_number_text_view);
            messageText = itemView.findViewById(R.id.message_content_text_view);
            messageStatus = itemView.findViewById(R.id.message_status_text_view);
        }
    }

}
