package com.startup.smartnoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.startup.smartnoteapp.Database.AppDatabase;
import com.startup.smartnoteapp.Database.Note;

public class EditorActivity extends AppCompatActivity {
    EditText editText;
    Note note;
    EditorViewModel editorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editText = findViewById(R.id.et_text);
        editorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        editorViewModel.liveNote.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                editText.setText(note.getText());
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            setTitle("New Note");
        } else {
            setTitle("Edit Note");
            int id = bundle.getInt("UPDATE");
            editorViewModel.loadNote(id);
        }


    }


    public void btnSave(View view) {
        if (TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show();
        } else {

            note = new Note(editText.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance
                            (getApplicationContext()).notesDao().insertNote(note);
                }
            }).start();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void btnUpdate(View view) {
        editorViewModel.insertNote(editText.getText().toString());
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editorViewModel.insertNote(editText.getText().toString());
    }

    public void deleteBtn(View view) {
        editorViewModel.deleteNote();
        finish();
    }
}
