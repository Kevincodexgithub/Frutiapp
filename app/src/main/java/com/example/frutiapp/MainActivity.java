package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_BestScore;
    private MediaPlayer mp;

    int num_aleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView)findViewById(R.id.imageView_Personaje);
        tv_BestScore = (TextView)findViewById(R.id.textView_BestScore);
        //Poner el icono en el accion bar de la app
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;
        //Mostrará aleatoreamente una imagen de la fruta, al abrir la aplicacion
        if(num_aleatorio == 0 || num_aleatorio == 10){
            id = getResources().getIdentifier("mango","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 1 || num_aleatorio == 9){
            id = getResources().getIdentifier("fresa","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("manzana","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 3 || num_aleatorio == 7){
            id = getResources().getIdentifier("sandia","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }else if(num_aleatorio == 4 || num_aleatorio == 5 || num_aleatorio == 6){
            id = getResources().getIdentifier("uva","drawable",getPackageName());
            iv_personaje.setImageResource(id);
        }
        //conexion a la base de datos sqlite
        AdminSqliteOpenHelper admin = new AdminSqliteOpenHelper(this,"BD",null,1);
        //Iniciar la apertura y escritura de la base de datos
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)",null
        );
        //verifica si existe algun valor en la base de datos
        if(consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_BestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        }else{
            BD.close();
        }

        //Inciar el audio de la aplicacion
        mp = MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        //cicla el audio
        mp.setLooping(true);
    }

    public void Jugar(View view){
        String nombre = et_nombre.getText().toString();

        if(!nombre.equals("")){
            mp.stop();
            //permite destruir el objeto de la classe mp, para ahorrar recursos
            mp.release();
            //ir de un activity a  otro
            Intent intent = new Intent(this,Main2Activity_Nivel1.class);
            //enviar datos al otro parametro
            intent.putExtra("jugador",nombre);
            //inciar el activity Main2Activity_Nivel1
            startActivity(intent);
            //Cerrar el activity actual
            finish();
        }else{
            Toast.makeText(this, "Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();
            //poner el foco en el txt_nombre
            et_nombre.requestFocus();
            //abre el teclado para ingresar el nombre
            InputMethodManager inn = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            inn.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);
        }
    }
    //controla el boton regresar de la aplicación
    @Override
    public void onBackPressed(){

    }
}
