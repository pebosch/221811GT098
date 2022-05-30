package org.ieszv.di.acl.pensamientocritico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {
    String questionsString = "1;https://andared.com/ieszaidinvergeles/pmdmnoticias2021/n1.png;V\n" +
                             "2;https://andared.com/ieszaidinvergeles/pmdmnoticias2021/n2.png;V\n" +
                             "3;https://andared.com/ieszaidinvergeles/pmdmnoticias2021/n3.png;V\n" +
                             "4;https://andared.com/ieszaidinvergeles/pmdmnoticias2021/n4.png;F\n" +
                             "5;https://andared.com/ieszaidinvergeles/pmdmnoticias2021/n5.png;V";
    ArrayList<String> questions;
    ArrayList<String> answers;
    ArrayList<String> cbChecked;
    Usuario user;
    int actualQuestion = 0;
    String respuestaUsuarioActual = "";
    String idPreguntaActual;
    String respuestaPreguntaActual;

    ImageView iv;
    Button btVerdadero, btFalso, btSiguiente;
    CheckBox cbTrue1, cbTrue2, cbTrue3, cbFalse1, cbFalse2,cbFalse3;
    Toolbar tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        tab = findViewById(R.id.toolbar);
        setSupportActionBar(tab);
        initialize();
    }

    //Inicializamos los componentes
    private void initialize() {
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        user = bundle.getParcelable("user");
        tab.setTitle(user.getId());

        questions = getQuestions();
        answers = new ArrayList<>();
        cbChecked = new ArrayList<>();

        cbTrue1 = findViewById(R.id.cbTrue1);
        cbTrue2 = findViewById(R.id.cbTrue2);
        cbTrue3 = findViewById(R.id.cbTrue3);
        cbFalse1 = findViewById(R.id.cbFalse1);
        cbFalse2 = findViewById(R.id.cbFalse2);
        cbFalse3 = findViewById(R.id.cbFalse3);
        iv = findViewById(R.id.ivQuestion);
        btVerdadero = findViewById(R.id.btVerdadero);
        btFalso = findViewById(R.id.btFalso);
        btSiguiente = findViewById(R.id.btSiguiente);

        cbTrue1.setVisibility(View.GONE);
        cbTrue2.setVisibility(View.GONE);
        cbTrue3.setVisibility(View.GONE);
        cbFalse1.setVisibility(View.GONE);
        cbFalse2.setVisibility(View.GONE);
        cbFalse3.setVisibility(View.GONE);

        btSiguiente.setOnClickListener((View v) -> {
            //Boton Siguiente
            if(!respuestaUsuarioActual.isEmpty()){
                String answer = loadAnswer();
                loadCB(answer);
                getNextQuestion();
            } else {
                alertNoAnswers();
            }
        });

        btVerdadero.setOnClickListener((View v) -> {
            cbTrue1.setVisibility(View.VISIBLE);
            cbTrue2.setVisibility(View.VISIBLE);
            cbTrue3.setVisibility(View.VISIBLE);
            cbFalse1.setVisibility(View.GONE);
            cbFalse2.setVisibility(View.GONE);
            cbFalse3.setVisibility(View.GONE);
            respuestaUsuarioActual = "V";
        });

        btFalso.setOnClickListener((View v) -> {
            cbTrue1.setVisibility(View.GONE);
            cbTrue2.setVisibility(View.GONE);
            cbTrue3.setVisibility(View.GONE);
            cbFalse1.setVisibility(View.VISIBLE);
            cbFalse2.setVisibility(View.VISIBLE);
            cbFalse3.setVisibility(View.VISIBLE);
            respuestaUsuarioActual = "F";
        });
        getNextQuestion();
    }

    private void loadCB(String respuesta) {
        String cbAnswer = "";
        if (respuesta.equals("1")){
            if(cbTrue1.isChecked())
                cbAnswer += "cb1;";
            if(cbTrue2.isChecked())
                cbAnswer += "cb2;";
            if(cbTrue3.isChecked())
                cbAnswer += "cb3;";
        } else {
            if(cbFalse1.isChecked())
                cbAnswer += "cb1;";
            if(cbFalse2.isChecked())
                cbAnswer += "cb2;";
            if(cbFalse3.isChecked())
                cbAnswer += "cb3;";
        }
        if (cbAnswer.isEmpty()){
            cbAnswer = "none";
        }
        cbChecked.add(cbAnswer);
    }

    private String loadAnswer() {
        String respuesta = idPreguntaActual + ";";
        if (respuestaUsuarioActual.equals(respuestaPreguntaActual))
            respuesta += "1";
        else
            respuesta += "0";
        answers.add(respuesta);
        return respuesta;
    }

    private void getNextQuestion(){
        if (actualQuestion != questions.size()) {
            String[] data = questions.get(actualQuestion).split(";");
            idPreguntaActual = data[0];
            Glide.with(this).load(data[1]).into(iv);
            respuestaPreguntaActual = data[2];
            respuestaUsuarioActual = "";
            actualQuestion++;
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("respuestas", answers);
            bundle.putStringArrayList("cbMarcados", cbChecked);
            bundle.putParcelable("user", user);
            Intent intent = new Intent(this, AnswersActivity.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
            finish();
        }
        cbTrue1.setVisibility(View.GONE);
        cbTrue2.setVisibility(View.GONE);
        cbTrue3.setVisibility(View.GONE);
        cbFalse1.setVisibility(View.GONE);
        cbFalse2.setVisibility(View.GONE);
        cbFalse3.setVisibility(View.GONE);

        cbTrue1.setChecked(false);
        cbTrue2.setChecked(false);
        cbTrue3.setChecked(false);
        cbFalse1.setChecked(false);
        cbFalse2.setChecked(false);
        cbFalse3.setChecked(false);
    }

    //Leemos el archivo
    private ArrayList<String> getQuestions(){
        /*ArrayList<String> questions = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = openFileInput("imagenes.txt");
            byte[] buffer = new byte[1024];
            int n;
            String question = "";
            while ((n = fis.read(buffer)) != -1){
                String s = Character.toString((char) n);
                if(!s.equals("\n")){
                    question += s;
                } else {
                    questions.add(s);
                    Log.v("xyzyx", s);
                    s = "";
                }
            }
            return questions;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }*/
        ArrayList<String> res = new ArrayList<>();
        String[] questions = questionsString.split("\n");
        for (String q : questions) {
            res.add(q);
        }
        return res;
    }

    private void alertNoAnswers(){

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Error");
        ad.setMessage("No hay respuesta")
                .setPositiveButton("OK", (dialog, which) -> {
                    //NO HAGAS NADA
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.optPrivacy) {
            gotoWeb("https://andared.com/pensamientocritico.php/privacidad.php");
            return true;
        }
        if (id == R.id.optAdmin) {
            gotoWeb("https://andared.com/pensamientocritico.php/admin.php");
            return true;
        }
        if (id == R.id.optExit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoWeb(String url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}