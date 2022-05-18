package com.example.intentimplicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private EditText etTelefono;
    private ImageButton btnLlamar, btnCamara;

    //
    private String numeroTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnLlamar.setOnClickListener( view -> {
            obtenerInformacion();
            configuraraIntentImplicito();
        });
    }

    private void inicializarVistas() {
        etTelefono = findViewById(R.id.etTelefono);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnCamara = findViewById(R.id.btnCamara);
    }

    private void obtenerInformacion() {
        numeroTelefono = etTelefono.getText().toString();
    }

    private void configuraraIntentImplicito() {
        //Primro validamos si el campo de tecto no esta vacio
        if(!numeroTelefono.isEmpty()){
            //Primer problema
            //Las llamadas han cambiado desde la version 6 o SDK23
            //A partir de esa vesion se hace el codigo con ciertos cambios
            //antes de esa version tenia otra manera de hacer codigo

            //Validar si la version de tu proyecto es mayor o igual
            //a la version de android donde cambio la firma de procesar la llamada
            //Ej: SDK_INT ej: VERSION_CODES codes.M = 23
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //para versones nuevas
            }
            else{
                //para versiones antiguas
                configurarVesionesAntiguas();
            }
        }
    }

    private void configurarVesionesAntiguas() {
        //Aqui vamos a configurara en intent para versiones
        //anteriores a la version cuando cambio el codigo
        //Intent Implicito -> 1) Que accion quieren realizar
        //                  2) Que datos quieres enviar eb ek Intent

        //UIR es como elurl de wrb conde configuras las cabeceras
        //de tu ruta donde quieres pasar datos.
        Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numeroTelefono));
        startActivity(intentLlamada);
    }

    private boolean revisarPermisos(String permiso){
        //Android maneja los permisos de esta manera:
        //GRANTED: permiso otorgado
        //DENIED: permiso no otorgado
        //Validar si el permiso a avelaiar eb su aplicaion
        //tiene el valor que Android maneja para un permiso
        return false;
    }
}