package com.ateeqoye.stickynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ateeqoye.stickynotes.activities.AddNewNotesActivity;
import com.ateeqoye.stickynotes.adapters.MyNotesAdapter;
import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.listeners.MyNotesListeners;
import com.example.stickynotes.R;

import java.util.ArrayList;
import java.util.List;

public class MyNotesFragment extends Fragment implements MyNotesListeners {

    ImageView addNotes;
    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int UPDATE_NOTE = 2;
    private static final int SHOW_NOTE = 3;
    private int clickedPosition = -1;

    private RecyclerView noteRec;
    private List<MyNotesEntities> notesEntitiesList;
    private MyNotesAdapter myNotesAdapter;

    public MyNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_my_notes, container, false);
        addNotes = view.findViewById (R.id.add_notes);
        addNotes.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivityForResult (new Intent (getContext () , AddNewNotesActivity.class ),REQUEST_CODE_ADD_NOTE);
            }
        });

        noteRec = view.findViewById (R.id.note_rec);
        noteRec.setLayoutManager (new StaggeredGridLayoutManager (2 , StaggeredGridLayoutManager.VERTICAL));
        notesEntitiesList = new ArrayList<> ();
        myNotesAdapter = new MyNotesAdapter (notesEntitiesList ,this);
        noteRec.setAdapter (myNotesAdapter);

        EditText inputSearch = view.findViewById (R.id.editText3);
        inputSearch.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myNotesAdapter.cancelTimer ();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(notesEntitiesList.size () != 0)
                {
                    myNotesAdapter.searchNote (s.toString ());

                }
            }
        });
        
        getAllNotes(SHOW_NOTE , false);

        return view;
    }

    private void getAllNotes(final int requestCode , final boolean isNoteDeleted) {

        class GetNoteTask extends AsyncTask<Void , Void , List<MyNotesEntities>>
        {
            @Override
            protected List<MyNotesEntities> doInBackground(Void... voids) {
                return MyNotesDatabase
                        .getMyNotesDatabase (getActivity ().getApplicationContext ())
                        .notesDao ()
                        .getAllNotes ();
            }

            @Override
            protected void onPostExecute(List<MyNotesEntities> myNotesEntities) {
                super.onPostExecute (myNotesEntities);

                if (requestCode == SHOW_NOTE)
                {
                    notesEntitiesList.addAll (myNotesEntities);
                    myNotesAdapter.notifyDataSetChanged ();
                }
                else if(requestCode == REQUEST_CODE_ADD_NOTE)
                {
                    notesEntitiesList.add (0 , myNotesEntities.get (0));
                    myNotesAdapter.notifyItemInserted (0);
                    noteRec.smoothScrollToPosition (0);
                }
                else if(requestCode == UPDATE_NOTE)
                {
                    notesEntitiesList.remove (clickedPosition);
                    if(isNoteDeleted)
                    {
                        myNotesAdapter.notifyItemRemoved (clickedPosition);
                    }
                    else
                    {
                        notesEntitiesList.add (clickedPosition, myNotesEntities.get (clickedPosition));
                        myNotesAdapter.notifyItemChanged (clickedPosition);
                    }
                }
            }
        }
        new GetNoteTask ().execute ();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getAllNotes (REQUEST_CODE_ADD_NOTE , false);
        }
        else if( requestCode == UPDATE_NOTE && resultCode == RESULT_OK)
        {
            if (data !=null)
            {
             getAllNotes (UPDATE_NOTE , data.getBooleanExtra ("isNoteDeleted" , false));
            }
        }
    }

    @Override
    public void myNoteClick(MyNotesEntities myNotesEntities, int position) {

        clickedPosition = position;
        Intent intent = new Intent (getContext ().getApplicationContext () , AddNewNotesActivity.class);
        intent.putExtra ("updateOrView" , true);
        intent.putExtra ("myNotes" , myNotesEntities);
        startActivityForResult (intent , UPDATE_NOTE);

    }
}