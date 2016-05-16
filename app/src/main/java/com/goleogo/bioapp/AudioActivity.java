package com.goleogo.bioapp;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class AudioActivity extends AppCompatActivity {
    Button btnAction;
    Button btnGuardar;
    EditText title;
    EditText description;

    boolean audio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        btnAction = (Button) findViewById(R.id.btnAction);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);


        //abrimos grabadora de audio
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMic();
                audio = true;
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audio == true) {
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setLatitude(MainActivity.location.getLatitude());
                    note.setLongitude(MainActivity.location.getLongitude());
                    note.setDescription(description.getText().toString());
                    note.setFilepathS3(audioPath());
                    note.setId_user(Login.user.getFacebookID());
                    note.setUser_firebase_key(Login.user_firebase_key);
                    note.setType("audio");

                    final Firebase refUsers = new Firebase("https://bioappleo.firebaseio.com/").child("notes").push();
                    refUsers.setValue(note);

                    Snackbar.make(view, "Enviado!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    audio = false;
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    Snackbar.make(view, "No hay grabado ningún audio", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });
    }


    public String audioPath() {
        //Cogemos el PATH del último audio
        String[] projection = { MediaStore.Audio.Media.DATA };
        Cursor cursor = this.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToLast();
        String path =  cursor.getString(column_index_data);

        return path;
    }

    public void openMic () {
        final int ACTIVITY_RECORD_SOUND = 1;
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
    }

}
