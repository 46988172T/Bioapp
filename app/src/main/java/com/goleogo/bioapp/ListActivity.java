package com.goleogo.bioapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable foto = getResources().getDrawable( R.drawable.photo );
        final Drawable audio = getResources().getDrawable( R.drawable.audio );
        final Drawable texto = getResources().getDrawable( R.drawable.text );
        final Drawable video = getResources().getDrawable( R.drawable.video );

        listView = (ListView) findViewById(R.id.listViewNotes);
        Firebase.setAndroidContext(this);

        final Firebase refNotas = new Firebase("https://bioappleo.firebaseio.com/").child("notes");

        final FirebaseListAdapter adapter = new FirebaseListAdapter<Note>(this, Note.class, R.layout.item_list, refNotas) {
            @Override
            protected void populateView(View v, final Note note, int position) {
                super.populateView(v, note, position);

                TextView titulo = (TextView) v.findViewById(R.id.tituloText);
                TextView descripcion = (TextView) v.findViewById(R.id.descripcionText);
                ImageView tipo = (ImageView) v.findViewById(R.id.tipoNota);
                titulo.setText(note.getTitle());
                descripcion.setText(note.getDescription());
                if(note.getType().equals("foto")){
                    tipo.setImageDrawable(foto);
                }else if(note.getType().equals("audio")){
                    tipo.setImageDrawable(audio);
                }else if(note.getType().equals("texto")){
                    tipo.setImageDrawable(texto);
                }else if(note.getType().equals("video")){
                    tipo.setImageDrawable(video);
                }
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), DetailActivity.class);
                final Firebase ref = adapter.getRef(position);
                Firebase refNota = new Firebase("https://bioappleo.firebaseio.com/").child("notes");
                refNota.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            final Note note = postsnapshot.getValue(Note.class);
                            if (postsnapshot.getRef().toString().equals(ref.toString())) {
                                intent.putExtra("nota", note);
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
}
