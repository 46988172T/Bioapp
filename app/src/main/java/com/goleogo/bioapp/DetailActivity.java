package com.goleogo.bioapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class DetailActivity extends AppCompatActivity {
    TextView titulo, descripcion;
    ImageView imagen;
    VideoView video;
    Note nota;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        titulo = (TextView) findViewById(R.id.titulo);
        imagen = (ImageView) findViewById(R.id.imagen);
        video = (VideoView) findViewById(R.id.video);

        descripcion = (TextView) findViewById(R.id.descripcion);

        nota = (Note) getIntent().getSerializableExtra("nota");

        imagen.setVisibility(View.INVISIBLE);
        video.setVisibility(View.INVISIBLE);
        titulo.setText(nota.getTitle());
        descripcion.setText(nota.getDescription());

        if (nota.getType().equals("foto")) {
            imagen.setVisibility(View.VISIBLE);
            Uri uri = Uri.fromFile(new File(nota.getFilepathS3()));
            Picasso.with(getBaseContext()).load(uri).resize(4096,4096).centerCrop().into(imagen);
        } else if (nota.getType().equals("video")) {
            MediaController mediaController = new MediaController(this);
            video.setVisibility(View.VISIBLE);
            video.setVideoPath(nota.getFilepathS3());
            video.setMediaController(mediaController);
            mediaController.hide();
            video.start();
        }
    }

}
