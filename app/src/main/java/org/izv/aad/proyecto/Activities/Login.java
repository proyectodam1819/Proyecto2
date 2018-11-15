package org.izv.aad.proyecto.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.izv.aad.proyecto.Logup;
import org.izv.aad.proyecto.R;

public class Login extends AppCompatActivity {

    private TextView tvLogup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        initLogUp();
    }

    private void init(){
        tvLogup = findViewById(R.id.tvLogup);
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
}
