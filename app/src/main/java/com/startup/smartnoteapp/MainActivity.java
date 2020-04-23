package com.startup.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.startup.smartnoteapp.adapter.AdapterClass;
import com.startup.smartnoteapp.room_db.Note;
import com.startup.smartnoteapp.view_models.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    AdapterClass adapterClass;
    List<Note> list = new ArrayList<>();
    NoteViewModel noteViewModel;
    private int position;

    /* onCreate() **********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        showRecyclerView();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        registerForContextMenu(recyclerView);
        position = adapterClass.getPosition();
        // Observing Live Data...
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.d(TAG, "onChanged: " + notes.toString());
                list.clear();
                list.addAll(notes);
                adapterClass.notifyDataSetChanged();
            }
        });
    }
    /* *********************************************************/

    // BREAK

    /*==========================================================================================================*/
    void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

    }

    void showRecyclerView() {
        adapterClass = new AdapterClass(this, list);
        recyclerView.setAdapter(adapterClass);
    }

    // Open Editor Fab...
    public void openEditor(View view) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.del_all:
                deleteAllNotes();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        noteViewModel.deleteAllNotes();
    }


    /*==========================================================================================================*/


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.edit_del_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // Delete...
            case R.id.del:
                Note note = list.get(position);
                noteViewModel.deleteNote(note);
                Toast.makeText(this, "Del" + adapterClass.getPosition(), Toast.LENGTH_SHORT).show();
                return true;
            // Edit...
            case R.id.edit:
                Note noteModel = list.get(position);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("UPDATE", noteModel.getId());
                startActivity(intent);
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
                return true;
            //
            default:
                return super.onContextItemSelected(item);

        }

    }

}
