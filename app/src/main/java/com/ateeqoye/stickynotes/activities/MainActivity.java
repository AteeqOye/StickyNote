package com.ateeqoye.stickynotes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ateeqoye.stickynotes.fragments.ShoppingListFragment;
import com.example.stickynotes.R;
import com.ateeqoye.stickynotes.fragments.MyNotesFragment;
import com.ateeqoye.stickynotes.fragments.ReminderFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        chipNavigationBar = findViewById (R.id.bottom_navigation_bar);
        chipNavigationBar.setItemSelected (R.id.home , true);
        getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container , new MyNotesFragment ()).commit ();

        bottomMenu();
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener (new ChipNavigationBar.OnItemSelectedListener () {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.home:
                    fragment = new MyNotesFragment ();
                    break;

                    case R.id.reminder:
                        fragment = new ReminderFragment ();
                        break;

                    case R.id.shopping_list:
                        fragment = new ShoppingListFragment ();
                        break;
                }
                getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container , fragment).commit ();
            }
        });

    }
}