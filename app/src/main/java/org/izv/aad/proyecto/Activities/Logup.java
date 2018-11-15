package org.izv.aad.proyecto.Activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Fragments.FragmentLogup1;
import org.izv.aad.proyecto.Fragments.FragmentLogup2;
import org.izv.aad.proyecto.Interfaces.IntefaceFireBase;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

public class Logup extends AppCompatActivity {

    private TextView create_mail,create_password, create_repitePassword;
    private TextInputLayout create_mailLayout, create_passwordLayout, create_repitePasswordLayout;
    private FragmentLogup1 fragmentLogup1;
    private FragmentLogup2 fragmentLogup2;
    private Button create_next,create_logup;
    private IntefaceFireBase intefaceFireBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);
        init();


        // Actions from buttons
        changeFragment();
        register();
    }



    private void init(){
        create_mailLayout = findViewById(R.id.create_mailLayout);
        create_passwordLayout = findViewById(R.id.create_passwordLayout);
        create_repitePasswordLayout = findViewById(R.id.create_repitePasswordLayout);
        create_mail = create_mailLayout.getEditText();
        create_password = create_passwordLayout.getEditText();
        create_repitePassword = create_repitePasswordLayout.getEditText();

        create_next = findViewById(R.id.create_next);
        create_logup = findViewById(R.id.create_logup);

        intefaceFireBase = new IntefaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful) {
                if(isSuccessful){

                }else{

                }
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
            public FirebaseUser getUserLogin() {
                return null;
            }
        };

        fragmentLogup1 = (FragmentLogup1)getSupportFragmentManager().findFragmentById(R.id.fragmento1);
        fragmentLogup2 = (FragmentLogup2)getSupportFragmentManager().findFragmentById(R.id.fragment2);

        fragmentLogup2.getView().setVisibility(View.GONE);
    }

    private void changeFragment() {
        create_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_mailLayout.setError(null);
                if(!create_mail.getText().toString().equals("")){
                    fragmentLogup1.getView().setVisibility(View.GONE);
                    fragmentLogup2.getView().setVisibility(View.VISIBLE);
                }else{
                    create_mailLayout.setError(getString(R.string.not_empty));
                }
            }
        });
    }

    private void register(){
        create_logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_passwordLayout.setError(null);
                create_repitePasswordLayout.setError(null);
                if(create_password.getText().toString().equals("") || create_repitePassword.getText().toString().equals("")) {
                    if (create_password.getText().toString().equals("")) {
                        create_passwordLayout.setError(getString(R.string.not_empty));
                    }
                    if (create_repitePassword.getText().toString().equals("")) {
                        create_repitePasswordLayout.setError(getString(R.string.not_empty));
                    }
                }else if(!create_repitePassword.getText().toString().equals(create_password.getText().toString())){
                    create_repitePasswordLayout.setError(getString(R.string.not_equals));
                }else{
                    FirebaseCustom.logup(Logup.this, create_mail.getText().toString(), create_password.getText().toString(), intefaceFireBase);
                }
            }
        });
    }




}
