package org.izv.aad.proyecto.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;

import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Dialogs.DialogCustom;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.CreateAuthor;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.Objects.DateCustom;
import org.izv.aad.proyecto.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageBooks extends AppCompatActivity {

    private Button btAddAutor;
    private CreateAuthor createAuthor;
    private Manager manager;
    private ProgressDialog progressDialog;
    private TextInputLayout ilFechIni,ilFechFin,ilTitle;
    private EditText etFechIni,etFechFin,etTitle;
    private Spinner sp;
    private RatingBar rtBar;
    private EditText summary;
    private Button btCreateBook;
    private List<Author> authors;
    private ImageView iVPhoto;
    private Uri uri;
    private CheckBox ch;
    private RadioGroup rdGroup;
    private ArrayList<String> authorsString;
    private ArrayAdapter<String> adaptadorSpiner;
    private static final int READ_REQUEST_CODE = 1;
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    private void init(){
        manager = new Manager(this);

        getAuthors();
        addAdapterSpinner();

        progressDialog = DialogCustom.showDialog(this);
        btAddAutor = findViewById(R.id.btAddAutor);

        createAuthor = methodInterface();

        ilFechFin = findViewById(R.id.book_dateFinishLayout);
        ilFechIni = findViewById(R.id.book_dateStartLayout);
        ilTitle = findViewById(R.id.book_TitleLayout);
        etFechIni = ilFechIni.getEditText();
        etFechFin = ilFechFin.getEditText();
        etTitle = ilTitle.getEditText();

        rtBar = findViewById(R.id.book_ratingBar);
        summary = findViewById(R.id.book_summary);
        btCreateBook=findViewById(R.id.book_createBook);
        ch=findViewById(R.id.checkBox);
        iVPhoto=findViewById(R.id.book_logo);
        rdGroup=findViewById(R.id.book_rdGroup);

        btAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void setListenerFoto() {
        iVPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);

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
                    alertDialog.dismiss();
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
                addItemToSpinner(authorFirebase);
                progressDialog.dismiss();
            }
        };
    }

    private void setListenerBook() {
        btCreateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateCustom fechIni;
                DateCustom fechFin;

                String title=etTitle.getText().toString();
                String author=sp.getSelectedItem().toString();

                fechIni = new DateCustom().setDate(etFechIni.getText().toString(), "d-m-y", getString(R.string.barra));
                fechFin = new DateCustom().setDate(etFechFin.getText().toString(), "d-m-y", getString(R.string.barra));

                Boolean favorite=ch.isChecked();
                String resume=summary.getText().toString();
                Float rating=rtBar.getRating();
                cleanError();
                if(checkTittle() && checkRadios()){
                    Book book=new Book(0,0,"ss",title,uri.toString(),resume,rating,favorite,fechIni,fechFin);
                }

            }
        });
    }

    private  boolean checkTittle(){
        boolean correct=true;
        if(etTitle.getText().toString().isEmpty()){
            ilTitle.setError(getString(R.string.not_empty));
            correct=false;
        }
        return correct;
    }

    private boolean checkRadios(){
        boolean correct=false;
        switch (rdGroup.getCheckedRadioButtonId()){
            case R.id.book_rdStarted:
                if(etFechIni.getText().toString().isEmpty()){
                    ilFechIni.setError(getString(R.string.not_empty));
                }else{
                    correct=true;
                }
            case R.id.book_rdFinish:
                if(etFechIni.getText().toString().isEmpty()
                        || etFechFin.getText().toString().isEmpty()) {

                    if (etFechIni.getText().toString().isEmpty()) {
                        ilFechIni.setError(getString(R.string.not_empty));
                    }
                    if (etFechFin.getText().toString().isEmpty()) {
                        ilFechFin.setError(getString(R.string.not_empty));
                    }
                }else{
                    correct=true;
                }
            default:
                correct=true;
        }
        return correct;
    }

    private void cleanError(){
        ilFechIni.setError(null);
        ilFechIni.setError(null);
        ilFechFin.setError(null);
    }

    private void fechClick(Context context, final EditText editText) {

        DatePickerDialog recogerFecha = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int actualMonth = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String reformatDay = (dayOfMonth < 10) ? getString(R.string.cero) + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String reformatMonth = (actualMonth < 10) ? getString(R.string.cero) + String.valueOf(actualMonth) : String.valueOf(actualMonth);
                //Muestro la fecha con el formato deseado
                editText.setText(reformatDay + getString(R.string.barra) + reformatMonth + getString(R.string.barra) + year);

            }

        }, anio, mes, dia);
        recogerFecha.show();
    }

    private void setListenerFechIni() {
        etFechIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechClick(ManageBooks.this, etFechIni);
            }
        });
    }

    private void setListenerFechFin() {
        etFechFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechClick(ManageBooks.this, etFechFin);
            }
        });
    }

    private void getAuthors(){
        authors = manager.getAllAuthor(null);
    }

    private void addAdapterSpinner(){
        sp = findViewById(R.id.book_writter);
        authorsString = new ArrayList<>();
        for(Author author : authors){
            authorsString.add(author.getName());
        }
        adaptadorSpiner = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, authorsString);
        sp.setAdapter(adaptadorSpiner);
    }

    private void addItemToSpinner(Author author){
        authors.add(author);
        authorsString.add(author.getName());
        adaptadorSpiner.notifyDataSetChanged();
    }



}
