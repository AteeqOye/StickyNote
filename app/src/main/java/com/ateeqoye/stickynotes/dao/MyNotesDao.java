package com.ateeqoye.stickynotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.ateeqoye.stickynotes.entities.ShoppingList;

import java.util.List;

@Dao
public interface MyNotesDao {

    //For MyNotesEntity
    @Query ("SELECT * FROM note ORDER BY id DESC")
    List<MyNotesEntities> getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(MyNotesEntities notesEntities);

    @Delete
    void deleteNotes(MyNotesEntities notesEntities);


    //For ReminderEntity
    @Query ("SELECT * FROM reminder ORDER BY id DESC")
    List<MyReminderEntities> getAllReminder();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(MyReminderEntities myReminderEntities);

    @Delete
    void deleteReminder(MyReminderEntities myReminderEntities);


    //For ShoppingList
    @Query ("SELECT * FROM shoppingList ORDER BY id DESC")
    List<ShoppingList> getAllShoppingList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShoppingList(ShoppingList shoppingList);

}
