package org.ieszv.di.acl.pensamientocritico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText etName, etFSurname, etSSurname;
    Spinner spCurso, spLetra, spCentro;
    Button btEmpezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    //Capturamos los componentes
    private void initialize() {
        etName = findViewById(R.id.etName);
        etFSurname = findViewById(R.id.etFSurname);
        etSSurname = findViewById(R.id.etSSurname);
        spCurso = findViewById(R.id.spCurso);
        spLetra = findViewById(R.id.spLetra);
        spCentro = findViewById(R.id.spCentro);
        btEmpezar = findViewById(R.id.btEmpezar);

        btEmpezar.setOnClickListener((View v) -> {
            empezar();
        });
    }

    private void empezar() {
        if (isCompleted()){
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", getUsuario());
            Intent intent = new Intent(this, QuestionsActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        } else {
            alertEmpty();
        }
    }

    //Comprueba que los componentes obligatorios tengan al menos 1 valor
    private boolean isCompleted(){
        return !(etName.getText().toString().isEmpty() || etFSurname.getText().toString().isEmpty() || etSSurname.getText().toString().isEmpty());
    }

    private void alertEmpty(){

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Error");
        ad.setMessage("Algunos campos están vacíos")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //NO HAGAS NADA
                    }
                })
                .setCancelable(false)
                .show();
    }

    private Usuario getUsuario(){
        String name = etName.getText().toString();
        String fSurname = etFSurname.getText().toString();
        String sSurname = etSSurname.getText().toString();
        String centro = spCentro.getSelectedItem().toString();
        String curso = spCurso.getSelectedItem().toString();
        String letraCurso = spLetra.getSelectedItem().toString();

        Usuario user = new Usuario(name, fSurname, sSurname, centro, curso, letraCurso);

        return user;
    }

}