package com.example.schedulemessenger.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.Model.MessageDao;
import com.example.schedulemessenger.Model.MessageDatabase;

import java.util.List;

public class MessageRepository {

    private MessageDao messageDao;
    private LiveData<List<Message>> allMessagesLiveData;

    // Constructor to assign necessary variables
    public MessageRepository(Application application) {
        MessageDatabase messageDbInstance = MessageDatabase.getInstance(application);
        messageDao = messageDbInstance.messageDao();
        allMessagesLiveData = messageDao.getAllMessages();
    }

    // Creating methods for all database operations specified

    public LiveData<List<Message>> getAllMessages() {
        return allMessagesLiveData;
    }

    public void insertMessage(Message message) {
        new InsertAsyncTask(messageDao).execute(message);
    }

    public void updateMessage(Message message) {
        new UpdateAsyncTask(messageDao).execute(message);
    }

    public void deleteMessage(Message message) {
        new DeleteAsyncTask(messageDao).execute(message);
    }

    public void deleteAllMessages() {
        new DeleteAllAsyncTask(messageDao).execute();
    }

    /* AsyncTask classes */

    private static class InsertAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao messageDao;

        private InsertAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.insertMessage(messages[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao messageDao;

        private UpdateAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.updateMessage(messages[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao messageDao;

        private DeleteAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.deleteMessage(messages[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private MessageDao messageDao;

        private DeleteAllAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            messageDao.deleteAllMessages();
            return null;
        }
    }

}
