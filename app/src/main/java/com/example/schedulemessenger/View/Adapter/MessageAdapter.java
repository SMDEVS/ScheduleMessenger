package com.example.schedulemessenger.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.R;

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

        int type = currentMessage.getMessageType();
        int imageResourceId = -1;
        if(type == 1) {
            imageResourceId = R.drawable.ic_sms;
        } else if(type == 2) {
            imageResourceId = R.drawable.ic_whatsapp;
        } else if(type == 3) {
            imageResourceId = R.drawable.ic_email;
        } else if(type == 4) {
            imageResourceId = R.drawable.ic_instagram;
        }

        /* Formatting timeString in a more presentable manner */
        String timeString = currentMessage.getTimeString().trim();
        int indexOfSlash = timeString.indexOf('/');
        int indexOfSpace = timeString.indexOf(' ');
        int indexOfColon = timeString.indexOf(':');
        String month = timeString.substring(0, indexOfSlash);
        String day = timeString.substring(indexOfSlash + 1, indexOfSlash + 3);
        String year = timeString.substring(indexOfSlash + 4, indexOfSpace);
        String time = timeString.substring(indexOfSpace + 1, indexOfColon + 3);

        if(timeString.endsWith("pm") && !time.startsWith("12")) {
            time = (Integer.parseInt(time.substring(0, 2)) + 12) + "" + time.substring(2);
        } else if(timeString.endsWith("am") && time.startsWith("12")) {
            time = "00" + "" + time.substring(2);
        }
        timeString = day + '/' + month + "/" + year + " " + time;

        if(type != -1) {
            holder.imageView.setImageResource(imageResourceId);
        }
        holder.phoneNumber.setText(currentMessage.getPhoneNumber());
        holder.messageStatus.setText(timeString);
        holder.messageText.setText(currentMessage.getMessageText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public Message getMessageAt(int position) {
        return messageList.get(position);
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        public TextView phoneNumber;
        public TextView messageText;
        public TextView messageStatus;
        public ImageView imageView;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            phoneNumber = itemView.findViewById(R.id.phone_number_text_view);
            messageText = itemView.findViewById(R.id.message_content_text_view);
            messageStatus = itemView.findViewById(R.id.message_status_text_view);
        }
    }

}
