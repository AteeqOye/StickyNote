package com.ateeqoye.stickynotes.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ateeqoye.stickynotes.adapters.MyNotesAdapter;
import com.ateeqoye.stickynotes.adapters.ShoppingListAdapter;
import com.ateeqoye.stickynotes.database.MyNotesDatabase;
import com.ateeqoye.stickynotes.entities.MyReminderEntities;
import com.ateeqoye.stickynotes.entities.ShoppingList;
import com.example.stickynotes.R;
import com.ateeqoye.stickynotes.activities.AddNewShoppingListActivity;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends Fragment {

    ImageView addShoppingList;
    private static final int REQUEST_CODE_ADD_NOTE = 1;

    RecyclerView recyclerView;
    List<ShoppingList> shoppingLists;
    ShoppingListAdapter shoppingListAdapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_shopping_list, container, false);
        addShoppingList = view.findViewById (R.id.add_shopping_list);
        addShoppingList.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivityForResult (new Intent (getContext (), AddNewShoppingListActivity.class), REQUEST_CODE_ADD_NOTE);
            }
        });


        recyclerView = view.findViewById (R.id.shopping_rec);
        recyclerView.setLayoutManager (new LinearLayoutManager (getContext (), RecyclerView.VERTICAL, false));
        shoppingLists = new ArrayList<> ();
        shoppingListAdapter = new ShoppingListAdapter (shoppingLists);
        recyclerView.setAdapter (shoppingListAdapter);

        getAllShoppingList ();

        return view;
    }

    private void getAllShoppingList() {

        class GetAllShoppingList extends AsyncTask<Void, Void, List<ShoppingList>> {

            @Override
            protected List<ShoppingList> doInBackground(Void... voids) {
                return MyNotesDatabase
                        .getMyNotesDatabase (getActivity ().getApplicationContext ())
                        .notesDao ()
                        .getAllShoppingList ();
            }

            @Override
            protected void onPostExecute(List<ShoppingList> shopping) {
                super.onPostExecute (shopping);
                if (shoppingLists.size () == 0) {
                    shoppingLists.addAll (shopping);
                    shoppingListAdapter.notifyDataSetChanged ();
                } else {
                    shoppingLists.add (0, shopping.get (0));
                    shoppingListAdapter.notifyItemInserted (0);
                }
                recyclerView.smoothScrollToPosition (0);
            }
        }
        new GetAllShoppingList ().execute ();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getAllShoppingList ();
        }
    }
}