package com.ateeqoye.stickynotes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.example.stickynotes.R;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    List<MyReminderEntities> reminderEntities;

    public ReminderAdapter(List<MyReminderEntities> reminderEntities) {
        this.reminderEntities = reminderEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (R.layout.remember_items , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setReminder(reminderEntities.get (position));
    }

    @Override
    public int getItemCount() {
        return reminderEntities.size ();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title, dateTime;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            title = itemView.findViewById (R.id.reminder_heading);
            dateTime = itemView.findViewById (R.id.date_reminder);
            view = itemView.findViewById (R.id.reminder_view);
        }

        public void setReminder(MyReminderEntities myReminderEntities) {

            title.setText (myReminderEntities.getTitle ());
            dateTime.setText (myReminderEntities.getDateTime ());


            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground ();
            if (myReminderEntities.getColor () != null)
            {
                gradientDrawable.setColor (Color.parseColor (myReminderEntities.getColor ()));
            }
            else
            {
                gradientDrawable.setColor (Color.parseColor ("#FFFB7B"));
            }
        }
    }
}
