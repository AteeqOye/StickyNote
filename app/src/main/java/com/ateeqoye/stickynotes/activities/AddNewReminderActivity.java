package com.ateeqoye.stickynotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.example.stickynotes.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewReminderActivity extends AppCompatActivity {

    private EditText title;
    private TextView textDateTime, saveReminder;
    private View view;
    private String selectReminderColor;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_new_reminder);

        saveReminder = findViewById (R.id.save_reminder);
        view = findViewById (R.id.view_reminder);
        title = findViewById (R.id.input_note_title);
        textDateTime = findViewById (R.id.textDateTimeReminder);
        img_back = findViewById (R.id.image_back);

        img_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        selectReminderColor = "#FF937B";

        saveReminder.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                saveReminder ();
            }

        });


        textDateTime.setText (new SimpleDateFormat ("EEEE, dd MMMM yy HH:mm a", Locale.getDefault ())
                .format (new Date ()));

        bottomSheet ();
        setViewColor();
    }

    private void saveReminder() {
        if (title.getText ().toString ().trim ().isEmpty ()) {
            Toast.makeText (this, "Note Title Can't be Empty", Toast.LENGTH_SHORT).show ();
        }


        final MyReminderEntities myReminderEntities = new MyReminderEntities ();
        myReminderEntities.setTitle (title.getText ().toString ());
        myReminderEntities.setDateTime (textDateTime.getText ().toString ());
        myReminderEntities.setColor (selectReminderColor);


        class SaveReminder extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                MyNotesDatabase
                        .getMyNotesDatabase (getApplicationContext ())
                        .notesDao ()
                        .insertReminder (myReminderEntities);

                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute (unused);

                Intent intent = new Intent ();
                setResult (RESULT_OK, intent);
                finish ();
            }
        }
        new SaveReminder ().execute ();

    }

    private void setViewColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground ();
        gradientDrawable.setColor (Color.parseColor (selectReminderColor));
    }

    private void bottomSheet() {
        final LinearLayout linearLayout = findViewById (R.id.reminder_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from (linearLayout);
        linearLayout.findViewById (R.id.bottom_text_reminder).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState () != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState (BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    bottomSheetBehavior.setState (BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        final ImageView imgColor1 = linearLayout.findViewById (R.id.Reminder_imageColor1);
        final ImageView imgColor2 = linearLayout.findViewById (R.id.Reminder_imageColor2);
        final ImageView imgColor3 = linearLayout.findViewById (R.id.Reminder_imageColor3);
        final ImageView imgColor4 = linearLayout.findViewById (R.id.Reminder_imageColor4);

        linearLayout.findViewById (R.id.viewColor1).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectReminderColor = "#FF937B";
                imgColor1.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor2).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectReminderColor = "#FFFB7B";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor3).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectReminderColor = "#ADFF7B";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor4.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor4).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectReminderColor = "#96FFEA";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (R.drawable.ic_baseline_done_24);
                setViewColor();
            }
        });
    }

}