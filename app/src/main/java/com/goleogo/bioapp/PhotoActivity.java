package com.goleogo.bioapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {
    Button btnAction;
    Button btnGuardar;
    EditText title;
    EditText description;
    ImageView thumb;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String path;
    boolean foto = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        btnAction = (Button) findViewById(R.id.btnAction);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        thumb = (ImageView) findViewById(R.id.imagenThumb);

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
                    note.setFilepathS3(path);
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
                } else {
                    Snackbar.make(view, "No has hecho ninguna foto!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

        });


    }

    public void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                //creamos el archivo de la foto
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            //si se ha creado, lo guardamos.
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //crea la imagen indicando el directorio, si es la primera foto, lo crea.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "BIOAPP_" + timeStamp + "_";

        File storageDir = new File("/storage/emulated/0/bioapp/media/images/"); //creamos una carpeta independiente
        if (storageDir.exists()){ //si existe, nada.

        }else if (!storageDir.exists()){ //si no, lo creamos.
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* nombre de la imagen */
                ".jpg",         /* formato */
                storageDir      /* directorio */
        );

        // pasamos el path para guardarlo.
        path = image.getAbsolutePath();
        return image;
    }

}
