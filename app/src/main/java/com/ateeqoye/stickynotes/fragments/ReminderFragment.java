package com.ateeqoye.stickynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ateeqoye.stickynotes.activities.AddNewReminderActivity;
import com.ateeqoye.stickynotes.adapters.MyNotesAdapter;
import com.ateeqoye.stickynotes.adapters.ReminderAdapter;
import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.example.stickynotes.R;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment {

    ImageView addReminder;
    private static final int REQUEST_CODE_ADD_NOTE = 1;


    private RecyclerView reminderRec;
    private List<MyReminderEntities> reminderEntitiesList;
    private ReminderAdapter reminderAdapter;

    public ReminderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_reminder, container, false);
        addReminder = view.findViewById (R.id.add_reminder);
        addReminder.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivityForResult (new Intent (getContext () , AddNewReminderActivity.class ),REQUEST_CODE_ADD_NOTE);
            }
        });

        reminderRec = view.findViewById (R.id.reminder_rec);
        reminderRec.setLayoutManager (new StaggeredGridLayoutManager (2 , StaggeredGridLayoutManager.VERTICAL));
        reminderEntitiesList = new ArrayList<> ();
        reminderAdapter = new ReminderAdapter (reminderEntitiesList);
        reminderRec.setAdapter (reminderAdapter);
        
        getAllReminder();

        return view;
    }

    private void getAllReminder() {

        class GetAllReminder extends AsyncTask<Void , Void , List<MyReminderEntities>>{

            @Override
            protected List<MyReminderEntities> doInBackground(Void... voids) {
                return MyNotesDatabase
                        .getMyNotesDatabase (getActivity ().getApplicationContext ())
                        .notesDao ()
                        .getAllReminder ();
            }

            @Override
            protected void onPostExecute(List<MyReminderEntities> myReminderEntities) {
                super.onPostExecute (myReminderEntities);
                if(reminderEntitiesList.size () == 0)
                {
                    reminderEntitiesList.addAll (myReminderEntities);
                    reminderAdapter.notifyDataSetChanged ();
                }
                else
                {
                    reminderEntitiesList.add (0 , myReminderEntities.get (0));
                    reminderAdapter.notifyItemInserted (0);
                }
                reminderRec.smoothScrollToPosition (0);
            }
        }
        new GetAllReminder ().execute ();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getAllReminder ();
        }
    }
}