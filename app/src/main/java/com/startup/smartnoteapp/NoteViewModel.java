package com.startup.smartnoteapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.startup.smartnoteapp.Database.AppDatabase;
import com.startup.smartnoteapp.Database.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NoteViewModel extends AndroidViewModel {

    AppDatabase appDatabase;
    Executor executor = Executors.newSingleThreadExecutor();

    public NoteViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
    }


    public LiveData<List<Note>> getAllNotes() {
        return appDatabase.notesDao().getAllNotes();
    }

    public void deleteAllNotes(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.notesDao().deleteAllNotes();
            }
        });
    }



}
