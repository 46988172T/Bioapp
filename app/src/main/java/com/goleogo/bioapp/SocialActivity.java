package com.goleogo.bioapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;

public class SocialActivity extends AppCompatActivity {
    String foto = Login.user.getPictureUrl();
    ImageView fotoPerfil;
    String nombre = Login.user.getName();
    TextView textNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);

        fotoPerfil = (ImageView)findViewById(R.id.fotoPerfil);
        Picasso.with(this).load(foto).into(fotoPerfil);

        textNombre = (TextView)findViewById(R.id.textNombre);
        textNombre.setText(nombre);


    }
}

