package com.example.notetakingapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNote;
    private Button buttonSave;
    private ListView listViewNotes;
    private ArrayList<String> notes;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "notesApp";
    private static final String NOTES_KEY = "notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNote = findViewById(R.id.editTextNote);
        buttonSave = findViewById(R.id.buttonSave);
        listViewNotes = findViewById(R.id.listViewNotes);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        notes = new ArrayList<>(loadNotes());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listViewNotes.setAdapter(adapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextNote.getText().toString();
                if (!note.isEmpty()) {
                    notes.add(note);
                    adapter.notifyDataSetChanged();
                    saveNotes();
                    editTextNote.setText("");
                }
            }
        });

        listViewNotes.setOnItemClickListener((parent, view, position, id) -> editNoteDialog(position));

        listViewNotes.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteNoteDialog(position);
            return true;
        });
    }

    private Set<String> loadNotes() {
        return sharedPreferences.getStringSet(NOTES_KEY, new HashSet<>());
    }

    private void saveNotes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(NOTES_KEY, new HashSet<>(notes));
        editor.apply();
    }

    private void editNoteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Note");

        final EditText input = new EditText(this);
        input.setText(notes.get(position));
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.set(position, input.getText().toString());
                adapter.notifyDataSetChanged();
                saveNotes();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteNoteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes.remove(position);
                adapter.notifyDataSetChanged();
                saveNotes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
