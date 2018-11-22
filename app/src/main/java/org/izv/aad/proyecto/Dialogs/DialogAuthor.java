package org.izv.aad.proyecto.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.CreateAuthor;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.R;

public class DialogAuthor extends Dialog{

    private Button positive, negative;
    private Activity activity;
    private CreateAuthor createAuthor;
    private TextInputLayout tilCreateAuthor;
    private ProgressDialog progressDialog;

    public DialogAuthor(@NonNull Activity activity, CreateAuthor createAuthor, ProgressDialog progressDialog) {
        super(activity);
        this.activity = activity;
        this.createAuthor = createAuthor;
        this.progressDialog = progressDialog;
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_author2);

        positive = dialog.findViewById(R.id.positive);
        negative = dialog.findViewById(R.id.negative);
        tilCreateAuthor = dialog.findViewById(R.id.tilCreateAuthor);
        final EditText edCreateAuthor = tilCreateAuthor.getEditText();

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edCreateAuthor.getText().toString().isEmpty()){
                    tilCreateAuthor.setError(activity.getString(R.string.not_empty));
                }else{
                    progressDialog.show();
                    Author author = new Author(edCreateAuthor.getText().toString());
                    createAuthor.create(author);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }
}
