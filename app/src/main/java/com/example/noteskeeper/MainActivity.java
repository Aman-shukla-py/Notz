package com.example.noteskeeper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.noteskeeper.Adapters.NotesListAdapter;
import com.example.noteskeeper.Database.roomDB;
import com.example.noteskeeper.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
{
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes> notes=new ArrayList<>();
    roomDB Database;
    FloatingActionButton fab_add;
    SearchView searchview_home;
    Notes selectedNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);
        searchview_home=findViewById(R.id.searchview_home);

        Database= roomDB.getInstance(this);
        notes=Database.mainDAO().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });

        searchview_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText)
    {
        List<Notes> filteredList=new ArrayList<>();
        for(Notes singleNote: notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase()))
            {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                Database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(Database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                Notes new_notes=(Notes)data.getSerializableExtra("note");
                Database.mainDAO().update(new_notes.getId(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(Database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter=new NotesListAdapter(MainActivity.this,notes,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent=new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView)
        {
            selectedNotes=new Notes();
            selectedNotes=notes;
            showPopup(cardView);

        }
    };

    private void showPopup(CardView cardView)
    {
        PopupMenu popupMenu=new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.pin:
                if (selectedNotes.isPinned()) {
                    Database.mainDAO().pin(selectedNotes.getId(), false);
                    Toast.makeText(MainActivity.this, "Unpinned!", Toast.LENGTH_SHORT).show();
                } else {
                    Database.mainDAO().pin(selectedNotes.getId(), true);
                    Toast.makeText(MainActivity.this, "Pinned!", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(Database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                Database.mainDAO().delete(selectedNotes);
                notes.remove(selectedNotes);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}