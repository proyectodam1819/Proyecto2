package org.izv.aad.proyecto.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

public class Login extends AppCompatActivity {

    private TextView tvLogup;
    private ImageView loginlogo;
    private TextInputEditText loginmailET;
    private TextInputLayout createmailLayout;
    private ImageView createicoMail;
    private TextInputEditText loginpasswordET;
    private TextInputLayout createpasswordLayout;
    private ImageView createicoPasswordIV;
    private Button loginbtLogin;
    private TextView textView5;
    private TextView textView2;
    private TextView loginlinkRegistrarTV;
    private TextView textViewError;
    private InterfaceFireBase interfaceFireBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        initLogUp();
    }

    private void init(){
        tvLogup = findViewById(R.id.tvLogup);
        loginlinkRegistrarTV = (TextView) findViewById(R.id.login_linkRegistrar);
        textView2 = (TextView) findViewById(R.id.tvLogup);
        textView5 = (TextView) findViewById(R.id.textView5);
        loginbtLogin = (Button) findViewById(R.id.login_btLogin);
        createicoPasswordIV = (ImageView) findViewById(R.id.create_icoPassword);
        createpasswordLayout = (TextInputLayout) findViewById(R.id.create_passwordLayout);
        loginpasswordET = (TextInputEditText) findViewById(R.id.login_password);
        createicoMail = (ImageView) findViewById(R.id.create_icoMail);
        createmailLayout = (TextInputLayout) findViewById(R.id.create_mailLayout);
        loginmailET = (TextInputEditText) findViewById(R.id.login_mail);
        loginlogo = (ImageView) findViewById(R.id.login_logo);
        textViewError=(TextView) findViewById(R.id.textView);
        interfaceFireBase = managerCallBack();
    }
    public void botonLoginClick(View v) {

        if(loginmailET.getText().toString().compareTo("")==0){
            createmailLayout.setError("introduzca un correo");
            createmailLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }
        if(loginpasswordET.getText().toString().compareTo("")==0){
            createpasswordLayout.setError("introduzca un correo");
            createpasswordLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }

        if(loginpasswordET.getText().toString().compareTo("")!=0 && loginmailET.getText().toString().compareTo("")!=0) {
            createmailLayout.setErrorEnabled(false);
            createpasswordLayout.setErrorEnabled(false);
            FirebaseCustom.login(this, loginmailET.getText().toString(), loginpasswordET.getText().toString(), interfaceFireBase);
            if (FirebaseCustom.getUser() == null) {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setTextColor(Color.RED);
            } else {
                textViewError.setVisibility(View.GONE);

            }
        }
    }

    private void initLogUp(){
        tvLogup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Logup.class);
                startActivity(intent);
            }
        });
    }

    private InterfaceFireBase managerCallBack(){
        return new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {

            }

            @Override
            public Book getBook(Book book) {
                return null;
            }

            @Override
            public Author getAuthor(Author author) {
                return null;
            }

            @Override
            public FirebaseUser getUserLogin(FirebaseUser user, String error) {
                Log.v("XYZ", user.toString()+": " + error);
                if(error.equals(getString(R.string.error_login_email))){
                    //Mostrar mensaje
                }else if(error.equals(getString(R.string.error_login_clave))){
                    //Mostrar mensaje
                }else{
                    //Devolver usuario
                }

                return null;
            }
        };
    }
}
