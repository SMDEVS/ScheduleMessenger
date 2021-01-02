package com.example.schedulemessenger.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.Repository.MessageRepository;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private MessageRepository messageRepository;
    private LiveData<List<Message>> allMessagesLiveData;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
        allMessagesLiveData = messageRepository.getAllMessages();
    }

    /* Wrapper methods for all the database operations specified in MessageRepository.java  */

    public void insertMessage(Message message) {
        messageRepository.insertMessage(message);
    }

    public void updateMessage(Message message) {
        messageRepository.updateMessage(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.deleteMessage(message);
    }

    public void deleteAllMessages() {
        messageRepository.deleteAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessagesLiveData;
    }

}
