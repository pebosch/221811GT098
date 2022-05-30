package org.ieszv.di.acl.pensamientocritico;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AnswersActivity extends AppCompatActivity {
    ArrayList<String> answers;
    ArrayList<String> cbMarcados;
    Usuario user;
    Toolbar tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);tab = findViewById(R.id.toolbar2);
        setSupportActionBar(tab);
        initialize();
    }

    private void initialize() {
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        answers = bundle.getStringArrayList("respuestas");
        cbMarcados = bundle.getStringArrayList("cbMarcados");
        user = bundle.getParcelable("user");
        tab.setTitle(user.getId());
        sendData();
        LinearLayout lyRespuestas = findViewById(R.id.lyRespuestas);
        for (String respuesta : answers) {
            String[] data = respuesta.split(";");
            String id = data[0];
            String valor = data[1];
            LinearLayout ly = new LinearLayout(this);
            ly.setOrientation(LinearLayout.HORIZONTAL);
            ImageView iv = new ImageView(this);
            TextView tv = new TextView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            if (valor.equals("1")){
                Glide.with(this).load("https://cdn.pixabay.com/photo/2013/07/13/10/48/check-157822_1280.png").into(iv);
                tv.setText("Respuesta " + id + ": Correcta");
            }else{
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/03/24/13/45/incorrect-294245_1280.png").into(iv);
                tv.setText("Respuesta " + id + ": Incorrecta");
            }
            lyRespuestas.addView(ly);
            ly.addView(iv);
            ly.addView(tv);

            Button btSiguiente = findViewById(R.id.btFinalizar);
            btSiguiente.setOnClickListener((View v) -> {
                finish();
            });
        }
    }

    private void sendData(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("andared.com")
                .appendPath("pensamientocritico")
                .appendPath("proceso.php")
                .appendQueryParameter("id", user.id)
                .appendQueryParameter("nombre", user.name)
                .appendQueryParameter("apellido1", user.fSurname)
                .appendQueryParameter("apellido2", user.sSurname);
        for (int i = 0; i < answers.size(); i++){
            String key = "respuesta" + i;
            builder.appendQueryParameter(key, answers.get(i));
            key = "cbMarcadosRespuesta" + i;
            builder.appendQueryParameter(key, cbMarcados.get(i));
        }
        String myUrl = builder.build().toString();

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