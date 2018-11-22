package org.izv.aad.proyecto.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Dialogs.DialogAuthor;
import org.izv.aad.proyecto.Dialogs.DialogCustom;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.CreateAuthor;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.R;

public class ManageBooks extends AppCompatActivity {

    TextInputLayout book_TitleLayout, book_dateStartLayout, book_dateFinishLayout;
    Button btAddAutor;
    CreateAuthor createAuthor;
    private FirebaseCustom firebaseCustom;
    private Manager manager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    private void init(){
        progressDialog = DialogCustom.showDialog(this);
        btAddAutor = findViewById(R.id.btAddAutor);
        manager = new Manager(this);
        createAuthor = methodInterface();

        btAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog(){
        DialogAuthor dialogAuthor = new DialogAuthor(this, createAuthor, progressDialog);
        dialogAuthor.showDialog();
    }

    private CreateAuthor methodInterface(){
        return new CreateAuthor() {
            @Override
            public void create(Author author) {
                long id = manager.insertAutor(author);
                author.setId(id);
                Author authorFirebase = firebaseCustom.saveAuthor(author);
                manager.updateAuthor(authorFirebase);
                progressDialog.dismiss();
            }
        };
    }



}
