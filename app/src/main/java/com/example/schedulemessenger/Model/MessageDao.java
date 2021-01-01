package com.example.schedulemessenger.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Update
    void updateMessage(Message message);

    @Delete
    void deleteMessage(Message message);

    @Query("DELETE FROM message_table")
    void deleteAllMessages();

    // Method returns LiveData which we can observe, to enable updates in real time
    @Query("SELECT * FROM message_table ORDER BY message_send_time ASC")
    LiveData<List<Message>> getAllMessages();

}
