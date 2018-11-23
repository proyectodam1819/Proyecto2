package org.izv.aad.proyecto.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Dialogs.DialogCustom;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.CreateAuthor;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.R;

public class ManageBooks extends AppCompatActivity {

    private Button btAddAutor;
    private CreateAuthor createAuthor;
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        Button negative = dialogView.findViewById(R.id.negative);
        Button positive = dialogView.findViewById(R.id.positive);
        final TextInputLayout tilCreateAuthor = dialogView.findViewById(R.id.tilCreateAuthor);
        alertDialog.show();

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edAuthor = tilCreateAuthor.getEditText();
                if(edAuthor.getText().toString().isEmpty()){
                    tilCreateAuthor.setError(getString(R.string.not_empty));
                }else {
                    progressDialog.show();
                    Author author = new Author(edAuthor.getText().toString());
                    createAuthor.create(author);
                }
            }
        });


    }

    private CreateAuthor methodInterface(){
        return new CreateAuthor() {
            @Override
            public void create(Author author) {
                long id = manager.insertAutor(author);
                author.setId(id);
                Author authorFirebase = FirebaseCustom.saveAuthor(author);
                manager.updateAuthor(authorFirebase);
                progressDialog.dismiss();
            }
        };
    }



}
