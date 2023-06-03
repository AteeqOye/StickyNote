package com.ateeqoye.stickynotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateeqoye.stickynotes.entities.ShoppingList;
import com.example.stickynotes.R;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    List<ShoppingList> shoppingLists;

    public ShoppingListAdapter(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (R.layout.shopping_list_item , parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setShoppingList(shoppingLists.get (position));

    }

    @Override
    public int getItemCount() {
        return shoppingLists.size ();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle , textDateTime , textNote;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            textTitle = itemView.findViewById (R.id.shopping_heading);
            textDateTime = itemView.findViewById (R.id.date_shop);
            textNote = itemView.findViewById (R.id.shopping_list_text);
        }

        public void setShoppingList(ShoppingList shoppingList) {

            textTitle.setText (shoppingList.getTitle ());
            textNote.setText (shoppingList.getNoteText ());
            textDateTime.setText (shoppingList.getDateTime ());
        }
    }
}
