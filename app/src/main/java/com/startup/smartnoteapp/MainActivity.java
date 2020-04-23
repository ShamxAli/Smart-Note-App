package com.startup.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.startup.smartnoteapp.Adapter.AdapterClass;
import com.startup.smartnoteapp.Database.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    AdapterClass adapterClass;
    List<Note> list = new ArrayList<>();
    NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        addList();
        showRecyclerView();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);


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


    // Open Editor Fab...
    public void openEditor(View view) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }


    void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

    }

    void showRecyclerView() {
        adapterClass = new AdapterClass(this, list);
        recyclerView.setAdapter(adapterClass);
    }

    void addList() {
        list.add(new Note("Hello"));
        list.add(new Note("abc"));
        list.add(new Note("def"));
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
}
