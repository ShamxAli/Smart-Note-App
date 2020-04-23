package com.startup.smartnoteapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.startup.smartnoteapp.Database.AppDatabase;
import com.startup.smartnoteapp.Database.Note;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {


    Executor executor = Executors.newSingleThreadExecutor();
    AppDatabase appDatabase;
    MutableLiveData<Note> liveNote = new MutableLiveData<>();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
    }


    public void loadNote(final int id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = appDatabase.notesDao().getNoteById(id);
                liveNote.postValue(note);
            }
        });
    }

    public void insertNote(final String string) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = liveNote.getValue();
                note.setText(string);
                Log.d("MYTAG", "ruLOGOOLOLLOn: " + note);
                appDatabase.notesDao().insertNote(note);
            }
        });

    }

    public void deleteNote() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = liveNote.getValue();
                appDatabase.notesDao().deleteNote(note);
            }
        });


    }



}
