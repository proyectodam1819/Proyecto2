package org.izv.aad.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.izv.aad.proyecto.Fragments.FragmentLogup1;
import org.izv.aad.proyecto.Fragments.FragmentLogup2;

public class Logup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logup);

        FragmentLogup1 fragmentLogup1 = (FragmentLogup1)getSupportFragmentManager().findFragmentById(R.id.fragmento1);
        FragmentLogup2 fragmentLogup2 = (FragmentLogup2)getSupportFragmentManager().findFragmentById(R.id.fragment2);

        fragmentLogup2.getView().setVisibility(View.GONE);
    }
}
