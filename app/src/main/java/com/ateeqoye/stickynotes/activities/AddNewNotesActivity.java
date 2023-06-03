package com.ateeqoye.stickynotes.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.example.stickynotes.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewNotesActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteText;
    private TextView textDateTime, saveNotes;
    private View indicator1, indicator2;
    private String selectedColor;

    private ImageView addImg , img_back;
    private String selectedImg;

    private MyNotesEntities alreadyAvailableNote;

    public static final int STORAGE_PERMISSION = 1;
    public static final int SELECTED_IMG = 1;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_new_notes);

        indicator1 = findViewById (R.id.viewIndicatorLeft);
        indicator2 = findViewById (R.id.viewIndicatorRight);
        saveNotes = findViewById (R.id.saveNotes);
        inputNoteText = findViewById (R.id.input_note_text);
        inputNoteTitle = findViewById (R.id.input_note_title);
        textDateTime = findViewById (R.id.textDateTime);
        addImg = findViewById (R.id.add_image_notes);
        img_back = findViewById (R.id.image_back);

        img_back.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        selectedColor = "#FF937B";
        selectedImg = "";

        if (getIntent ().getBooleanExtra ("updateOrView" , false))
        {
            alreadyAvailableNote = (MyNotesEntities) getIntent ().getSerializableExtra ("myNotes");
            setViewUpdate();
        }

        saveNotes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                saveNotes ();
            }
        });

        textDateTime.setText (new SimpleDateFormat ("EEEE, dd MMMM yy HH:mm a", Locale.getDefault ())
                .format (new Date ()));

        findViewById (R.id.remove_image_notes).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                addImg.setImageBitmap (null);
                addImg.setVisibility (View.GONE);
                findViewById (R.id.remove_image_notes).setVisibility (View.GONE);

                selectedImg = "";
            }
        });

        bottomSheet ();
        setViewColor();
    }

    private void setViewUpdate() {
        inputNoteTitle.setText (alreadyAvailableNote.getTitle ());
        inputNoteText.setText (alreadyAvailableNote.getNoteText ());
        textDateTime.setText (alreadyAvailableNote.getDateTime ());

        if(alreadyAvailableNote.getImgPath () != null && !alreadyAvailableNote.getImgPath ().trim ().isEmpty ())
        {
            addImg.setImageBitmap (BitmapFactory.decodeFile (alreadyAvailableNote.getImgPath ()));
            addImg.setVisibility (View.VISIBLE);
            findViewById (R.id.remove_image_notes).setVisibility (View.VISIBLE);
            selectedImg = alreadyAvailableNote.getImgPath ();
        }
    }

    private void setViewColor() {
        GradientDrawable gradientDrawable =(GradientDrawable) indicator1.getBackground ();
        gradientDrawable.setColor (Color.parseColor (selectedColor));

        GradientDrawable gradientDrawable2 =(GradientDrawable) indicator2.getBackground ();
        gradientDrawable2.setColor (Color.parseColor (selectedColor));
    }

    private void saveNotes() {
        if (inputNoteTitle.getText ().toString ().trim ().isEmpty ()) {
            Toast.makeText (this, "Note Title Can't be Empty", Toast.LENGTH_SHORT).show ();
        } else if (inputNoteText.getText ().toString ().trim ().isEmpty ()) {
            Toast.makeText (this, "Note Text Can't be Empty", Toast.LENGTH_SHORT).show ();
        }

        final MyNotesEntities myNotesEntities = new MyNotesEntities ();
        myNotesEntities.setTitle (inputNoteTitle.getText ().toString ());
        myNotesEntities.setNoteText (inputNoteText.getText ().toString ());
        myNotesEntities.setDateTime (textDateTime.getText ().toString ());
        myNotesEntities.setColor (selectedColor);
        myNotesEntities.setImgPath (selectedImg);

          if (alreadyAvailableNote != null)
          {
               myNotesEntities.setId (alreadyAvailableNote.getId ());
          }

        class SaveNotes extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {

                MyNotesDatabase
                        .getMyNotesDatabase (getApplicationContext ())
                        .notesDao ()
                        .insertNote (myNotesEntities);

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
    private void bottomSheet()
    {
        final LinearLayout linearLayout = findViewById (R.id.bottom_sheet_notes);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from (linearLayout);
        linearLayout.findViewById (R.id.bottom_text_notes).setOnClickListener (new View.OnClickListener () {
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
        final ImageView imgColor1 = linearLayout.findViewById (R.id.imageColor1);
        final ImageView imgColor2 = linearLayout.findViewById (R.id.imageColor2);
        final ImageView imgColor3 = linearLayout.findViewById (R.id.imageColor3);
        final ImageView imgColor4 = linearLayout.findViewById (R.id.imageColor4);
        final ImageView imgColor5 = linearLayout.findViewById (R.id.imageColor5);
        final ImageView imgColor6 = linearLayout.findViewById (R.id.imageColor6);

        linearLayout.findViewById (R.id.viewColor1).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#FF937B";
                imgColor1.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                imgColor5.setImageResource (0);
                imgColor6.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor2).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#FFFB7B";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                imgColor5.setImageResource (0);
                imgColor6.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor3).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#ADFF7B";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor4.setImageResource (0);
                imgColor5.setImageResource (0);
                imgColor6.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor4).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#96FFEA";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor5.setImageResource (0);
                imgColor6.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor5).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#969CFF";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                imgColor5.setImageResource (R.drawable.ic_baseline_done_24);
                imgColor6.setImageResource (0);
                setViewColor();
            }
        });
        linearLayout.findViewById (R.id.viewColor6).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                selectedColor = "#FF96F5";
                imgColor1.setImageResource (0);
                imgColor2.setImageResource (0);
                imgColor3.setImageResource (0);
                imgColor4.setImageResource (0);
                imgColor5.setImageResource (0);
                imgColor6.setImageResource (R.drawable.ic_baseline_done_24);
                setViewColor();
            }
        });

        //
        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor () != null  && !alreadyAvailableNote.getColor ().trim ().isEmpty())
        {
            switch (alreadyAvailableNote.getColor ())
            {
                case "#FFFB7B":
                    linearLayout.findViewById (R.id.viewColor2).performClick ();
                    break;
                case "#ADFF7B":
                    linearLayout.findViewById (R.id.viewColor3).performClick ();
                    break;
                case "#96FFEA":
                    linearLayout.findViewById (R.id.viewColor4).performClick ();
                    break;
                case "#969CFF":
                    linearLayout.findViewById (R.id.viewColor5).performClick ();
                    break;
                case "#FF96F5":
                    linearLayout.findViewById (R.id.viewColor6).performClick ();
                    break;
            }
        }

        //Add Images
        linearLayout.findViewById (R.id.add_img).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                bottomSheetBehavior.setState (BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission (
                        getApplicationContext () , Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions (
                            AddNewNotesActivity.this ,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION
                    );
                }
                else
                {
                    selectYourImage();
                }
            }
        });
        if(alreadyAvailableNote !=null)
        {
            linearLayout.findViewById (R.id.remove).setVisibility (View.VISIBLE);
            linearLayout.findViewById (R.id.remove).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState (BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });

        }
    }

    private void showDeleteDialog() {

        if(alertDialog == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder (AddNewNotesActivity.this);
            View view = LayoutInflater.from (this).inflate (R.layout.layout_delete_note,
                    (ViewGroup) findViewById (R.id.layoutDeleteNote_Container));

            builder.setView (view);
            alertDialog = builder.create ();

            if(alertDialog.getWindow () != null)
            {
                alertDialog.getWindow ().setBackgroundDrawable (new ColorDrawable (0));
            }
            view.findViewById (R.id.textDeleteNote).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    class DeleteNoteTask extends AsyncTask<Void , Void , Void>{

                        @Override
                        protected Void doInBackground(Void... voids) {

                            MyNotesDatabase
                                    .getMyNotesDatabase (getApplicationContext ())
                                    .notesDao ()
                                    .deleteNotes (alreadyAvailableNote);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute (unused);

                            Intent intent = new Intent ();
                            intent.putExtra ("isNoteDeleted" , true);
                            setResult (RESULT_OK , intent);
                            finish ();
                        }
                    }
                    new DeleteNoteTask ().execute ();
                }
            });

            view.findViewById (R.id.textCancelNote).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss ();
                }
            });
        }
        alertDialog.show ();
    }

    private void selectYourImage() {

        Intent intent = new Intent (Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity (getPackageManager ()) != null)
        {
            startActivityForResult (intent , SELECTED_IMG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION && grantResults.length > 0)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                selectYourImage ();
            }
            else
            {
                Toast.makeText (this, "Permission Denied!", Toast.LENGTH_SHORT).show ();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);

        if (requestCode == SELECTED_IMG && resultCode == RESULT_OK)
        {
            if(data != null)
            {
                Uri selectImageUri = data.getData ();
                if (selectImageUri != null)
                {
                    try {

                        InputStream inputStream =getContentResolver ().openInputStream (selectImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream (inputStream);
                        addImg.setImageBitmap (bitmap);
                        addImg.setVisibility (View.VISIBLE);
                        selectedImg = getPathFromUri (selectImageUri);
                        findViewById (R.id.remove_image_notes).setVisibility (View.VISIBLE);
                    }catch (Exception e)
                    {
                        Toast.makeText (this, e.getMessage (), Toast.LENGTH_SHORT).show ();
                    }
                }
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPathFromUri(Uri uri)
    {
        String filePath;
        Cursor cursor = getContentResolver ()
                .query (uri , null , null , null);

        if (cursor == null)
        {
            filePath = uri.getPath ();
        }
        else
        {
            cursor.moveToFirst ();
            int index = cursor.getColumnIndex ("_data");
            filePath = cursor.getString (index);
            cursor.close ();
        }
        return filePath;
    }
}