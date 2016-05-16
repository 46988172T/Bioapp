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

public class PhotoActivity extends AppCompatActivity {
    Button btnAction;
    Button btnGuardar;
    EditText title;
    EditText description;

    boolean foto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        btnAction = (Button) findViewById(R.id.btnAction);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);


        //abrimos grabadora de audio
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                foto = true;
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foto == true) {
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setLatitude(MainActivity.location.getLatitude());
                    note.setLongitude(MainActivity.location.getLongitude());
                    note.setDescription(description.getText().toString());
                    note.setFilepathS3(photoPath());
                    note.setId_user(Login.user.getFacebookID());
                    note.setUser_firebase_key(Login.user_firebase_key);
                    note.setType("foto");

                    final Firebase refUsers = new Firebase("https://bioappleo.firebaseio.com/").child("notes").push();
                    refUsers.setValue(note);

                    Snackbar.make(view, "Enviado!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    foto = false;
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    Snackbar.make(view, "No has hecho ninguna foto!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });

    }
    public String photoPath() {
        //Cogemos el PATH de la ultima foto
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();
        String path =  cursor.getString(column_index_data);

        return path;
    }

    public void openCamera(){
        final int REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


}
