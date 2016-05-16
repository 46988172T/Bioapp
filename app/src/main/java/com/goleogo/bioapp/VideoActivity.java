package com.goleogo.bioapp;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class VideoActivity extends AppCompatActivity {
    Button btnAction;
    Button btnGuardar;
    EditText title;
    EditText description;

    boolean video = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        btnAction = (Button) findViewById(R.id.btnAction);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);


        //abrimos grabadora de audio
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVideo();
                video = true;
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (video == true) {
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setLatitude(MainActivity.location.getLatitude());
                    note.setLongitude(MainActivity.location.getLongitude());
                    note.setDescription(description.getText().toString());
                    note.setFilepathS3(videoPath());
                    note.setId_user(Login.user.getFacebookID());
                    note.setUser_firebase_key(Login.user_firebase_key);
                    note.setType("video");

                    final Firebase refUsers = new Firebase("https://bioappleo.firebaseio.com/").child("notes").push();
                    refUsers.setValue(note);

                    Snackbar.make(view, "Enviado!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    video = false;
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Snackbar.make(view, "No hay grabado ningún vídeo", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });
    }

    public String videoPath() {
        //Cogemos el PATH del último audio
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = this.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToLast();
        String path =  cursor.getString(column_index_data);

        return path;
    }

    public void openVideo() {
        final int REQUEST_VIDEO_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
}
