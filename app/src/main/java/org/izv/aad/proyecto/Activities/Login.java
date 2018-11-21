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
    private TextView loginlinkRegistrarTV;
    private TextView textViewError;
    private InterfaceFireBase interfaceFireBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initLogUp();
        setClickLogin();

    }

    private void init(){
        tvLogup = findViewById(R.id.tvLogup);
        loginlinkRegistrarTV = findViewById(R.id.login_linkRegistrar);
        textView5 = findViewById(R.id.textView5);
        loginbtLogin = findViewById(R.id.login_btLogin);
        createicoPasswordIV = findViewById(R.id.create_icoPassword);
        createpasswordLayout = findViewById(R.id.create_passwordLayout);
        loginpasswordET = findViewById(R.id.login_password);
        createicoMail = findViewById(R.id.create_icoMail);
        createmailLayout = findViewById(R.id.create_mailLayout);
        loginmailET = findViewById(R.id.login_mail);
        loginlogo = findViewById(R.id.login_logo);
        textViewError=findViewById(R.id.textView);
        interfaceFireBase = managerCallBack();
    }

    public void botonLoginClick() {
        if(loginmailET.getText().toString().compareTo("")==0){
            createmailLayout.setError(getString(R.string.msg_no_text_in_email));
            createmailLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }
        if(loginpasswordET.getText().toString().compareTo("")==0){
            createpasswordLayout.setError(getString(R.string.msg_no_text_in_contraseña));
            createpasswordLayout.setErrorEnabled(true);
            textViewError.setVisibility(View.GONE);
        }

        if(loginpasswordET.getText().toString().compareTo("")!=0 && loginmailET.getText().toString().compareTo("")!=0) {
            createmailLayout.setErrorEnabled(false);
            createpasswordLayout.setErrorEnabled(false);
            FirebaseCustom.login(this, loginmailET.getText().toString(), loginpasswordET.getText().toString(), interfaceFireBase);

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

    private void setClickLogin(){
        loginbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonLoginClick();
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
            public void getUserLogin(FirebaseUser user, String error) {
                if(error.equals(getString(R.string.error_login_email))){
                    createmailLayout.setError(getString(R.string.msg_login_email));
                    createmailLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else if(error.equals(getString(R.string.error_login_clave))){
                    createpasswordLayout.setError(getString(R.string.msg_login_clave));
                    createpasswordLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else if(error.equals(getString(R.string.error_login_no_email))) {
                    createmailLayout.setError(getString(R.string.msg_login_no_email));
                    createmailLayout.setErrorEnabled(true);
                    textViewError.setVisibility(View.GONE);
                }else{
                    Intent i = new Intent(Login.this, Index.class);
                    startActivity(i);
                }
            }


        };
    }
}
