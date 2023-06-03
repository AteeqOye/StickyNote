package com.ateeqoye.stickynotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.entities.ShoppingList;
import com.example.stickynotes.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddNewShoppingListActivity extends AppCompatActivity {

    private ImageView img_back;
    private EditText inputNoteTitleList, inputNoteTextList;
    private TextView textDateTime , saveNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_new_shopping_list);

        inputNoteTextList = findViewById (R.id.text_list);
        inputNoteTitleList = findViewById (R.id.input_note_title_list);
        textDateTime = findViewById (R.id.textDateTimeList);
        saveNotes = findViewById (R.id.save_shoppingList);
        img_back = findViewById (R.id.image_back);

        textDateTime.setText (new SimpleDateFormat ("EEEE, dd MMMM yy HH:mm a", Locale.getDefault ())
                .format (new Date ()));

        img_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        saveNotes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                saveNotes ();
            }
        });
    }

    private void saveNotes() {
        if (inputNoteTitleList.getText ().toString ().trim ().isEmpty ()) {
            Toast.makeText (this, "Note Title Can't be Empty", Toast.LENGTH_SHORT).show ();
        } else if (inputNoteTextList.getText ().toString ().trim ().isEmpty ()) {
            Toast.makeText (this, "Note Text Can't be Empty", Toast.LENGTH_SHORT).show ();
        }

        final ShoppingList shoppingList = new ShoppingList ();
        shoppingList.setTitle (inputNoteTitleList.getText ().toString ());
        shoppingList.setNoteText (inputNoteTextList.getText ().toString ());
        shoppingList.setDateTime (textDateTime.getText ().toString ());

        class SaveNotes extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                MyNotesDatabase
                        .getMyNotesDatabase (getApplicationContext ())
                        .notesDao ()
                        .insertShoppingList (shoppingList);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute (aVoid);

                Intent intent = new Intent ();
                setResult (RESULT_OK , intent);
                finish ();
            }
        }
        new SaveNotes ().execute ();
    }

}