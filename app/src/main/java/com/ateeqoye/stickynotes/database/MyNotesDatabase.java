package com.ateeqoye.stickynotes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ateeqoye.stickynotes.dao.MyNotesDao;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.ateeqoye.stickynotes.entities.ShoppingList;

@Database (entities = {MyNotesEntities.class , MyReminderEntities.class , ShoppingList.class} , version = 1, exportSchema = false)
public abstract class MyNotesDatabase extends RoomDatabase {


    private static MyNotesDatabase myNotesDatabase;

    public static synchronized MyNotesDatabase getMyNotesDatabase(Context context)
    {
        if (myNotesDatabase == null)
        {
            myNotesDatabase = Room.databaseBuilder (
                    context,
                    MyNotesDatabase.class,
                    "note_db"
            ).build ();

        }
        return myNotesDatabase;
    }

    public abstract MyNotesDao notesDao();

}
