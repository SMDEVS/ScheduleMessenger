package com.example.schedulemessenger.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Message.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    private static MessageDatabase messageDbInstance;

    public abstract MessageDao messageDao();

    public static synchronized MessageDatabase getInstance(Context context) {
        if(messageDbInstance == null) {
            messageDbInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MessageDatabase.class, "message_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return messageDbInstance;
    }

}
