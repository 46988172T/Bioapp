package com.goleogo.bioapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class TextActivity extends AppCompatActivity {
    Button btnGuardar;
    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.equals("") || description.equals("")) {
                    Snackbar.make(view, "Debes escribir en ambos campos!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setLatitude(MainActivity.location.getLatitude());
                    note.setFilepathS3("");
                    note.setLongitude(MainActivity.location.getLongitude());
                    note.setDescription(description.getText().toString());
                    note.setId_user(Login.user.getFacebookID());
                    note.setUser_firebase_key(Login.user_firebase_key);
                    note.setType("texto");

                    final Firebase refUsers = new Firebase("https://bioappleo.firebaseio.com/").child("notes").push();
                    refUsers.setValue(note);

                    Snackbar.make(view, "Enviado!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
            }

        });
    }
}
