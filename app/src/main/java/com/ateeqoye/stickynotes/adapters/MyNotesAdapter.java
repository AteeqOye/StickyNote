package com.ateeqoye.stickynotes.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateeqoye.stickynotes.entities.MyNotesEntities;
import com.ateeqoye.stickynotes.listeners.MyNotesListeners;
import com.example.stickynotes.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.ViewHolder> {

    List<MyNotesEntities> notesEntitiesList;
    MyNotesListeners myNotesListeners;

    private List<MyNotesEntities> noteSearch;
    private Timer timer;

    public MyNotesAdapter(List<MyNotesEntities> notesEntitiesList, MyNotesListeners myNotesListeners) {
        this.notesEntitiesList = notesEntitiesList;
        this.myNotesListeners = myNotesListeners;
        noteSearch = notesEntitiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (R.layout.note_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setNote(notesEntitiesList.get (position));
        holder.linearLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                myNotesListeners.myNoteClick (notesEntitiesList.get (position) ,position );
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesEntitiesList.size ();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle , textNode , dateTime;
        private LinearLayout linearLayout;
        RoundedImageView roundedImageView;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            textTitle = itemView.findViewById (R.id.textTitle);
            textNode = itemView.findViewById (R.id.textNote);
            dateTime = itemView.findViewById (R.id.textDateTimeNote);
            linearLayout = itemView.findViewById (R.id.layoutNote);
            roundedImageView = itemView.findViewById (R.id.imageNote_item);

        }

        public void setNote(MyNotesEntities notesEntities) {
            textTitle.setText (notesEntities.getTitle ());
            textNode.setText (notesEntities.getNoteText ());
            dateTime.setText (notesEntities.getDateTime ());

            GradientDrawable gradientDrawable = (GradientDrawable) linearLayout.getBackground ();
            if (notesEntities.getColor () != null)
            {
                gradientDrawable.setColor (Color.parseColor (notesEntities.getColor ()));
            }
            else
            {
                gradientDrawable.setColor (Color.parseColor ("#FF937B"));
            }
            if (notesEntities.getImgPath () != null)
            {
                roundedImageView.setImageBitmap (BitmapFactory.decodeFile (notesEntities.getImgPath ()));
                roundedImageView.setVisibility (View.VISIBLE);
            }
            else
            {
                roundedImageView.setVisibility (View.GONE);
            }
        }
    }
    public void searchNote(final String searchKeyWord)
    {
        timer = new Timer ();
        timer.schedule (new TimerTask () {
            @Override
            public void run() {

                if(searchKeyWord.trim ().isEmpty ())
                {
                    notesEntitiesList = noteSearch;
                }
                else
                {
                    ArrayList<MyNotesEntities> temp = new ArrayList<> ();
                    for(MyNotesEntities entities : noteSearch)
                    {
                        if(entities.getTitle ().toLowerCase ().contains (searchKeyWord.toLowerCase ())
                        || entities.getNoteText ().toLowerCase ().contains (searchKeyWord.toLowerCase ()))
                        {
                            temp.add (entities);
                        }
                    }
                    notesEntitiesList = temp;
                }
                new Handler (Looper.getMainLooper ()).post (new Runnable () {
                    @Override
                    public void run() {
                        notifyDataSetChanged ();
                    }
                });
            }
        } , 500);
    }

    public void cancelTimer()
    {
        if (timer != null)
        {
            timer.cancel ();
        }
    }
}
