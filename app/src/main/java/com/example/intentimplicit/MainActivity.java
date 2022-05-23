package com.example.intentimplicit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etTelefono;
    private ImageButton btnLlamar, btnCamara;
    private ImageView ivImagen;

    //
    private String numeroTelefono;

    //atributos que representen codigos de servicios de celular
    private final int PHONE_CODE = 100;
    private final  int CAMERA_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVistas();
        btnLlamar.setOnClickListener( view -> {
            obtenerInformacion();
            //etTelefono.setText("hola"+numeroTelefono);
            configuraraIntentImplicito();
        });
        btnCamara.setOnClickListener(view -> activarCamara());
    }

    private void inicializarVistas() {
        etTelefono = findViewById(R.id.etTelefono);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnCamara = findViewById(R.id.btnCamara);
        ivImagen = findViewById(R.id.ivImagen);
    }

    private void obtenerInformacion() {
        numeroTelefono = etTelefono.getText().toString();
    }

    private void configuraraIntentImplicito() {
        //Primro validamos si el campo de texto no esta vacio
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
                //Versiones superiores a la 23 se usan funciones de las librerias de Android
                //e incorporan un asunto llamado asincronia
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PHONE_CODE);
            }
            else{
                //para versiones antiguas
                configurarVesionesAntiguas();
            }
        }
    }

    private void versionesNuevas() {

    }

    private void configurarVesionesAntiguas() {
        //Aqui vamos a configurara en intent para versiones
        //anteriores a la version cuando cambio el codigo
        //Intent Implicito -> 1) Que accion quieren realizar
        //                    2) Que datos quieres enviar eb ek Intent

        //UIR es como elurl de wrb conde configuras las cabeceras
        //de tu ruta donde quieres pasar datos.
        if(revisarPermisos(Manifest.permission.CALL_PHONE)){
            Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numeroTelefono));
            startActivity(intentLlamada);
        }
        else{
            Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT);
        }
    }

    private boolean revisarPermisos(String permiso){
        //Android maneja los permisos de esta manera:
        //GRANTED: permiso otorgado 0
        //DENIED: permiso no otorgado -1
        //Validar si el permiso a avelaiar eb su aplicaion
        //tiene el valor que Android maneja para un permiso
        int resultadoPremiso = checkCallingOrSelfPermission(permiso);
        return resultadoPremiso == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PHONE_CODE:
                String permiso = permissions[0];
                int valorPermiso = grantResults[0];
                //para evitar que halla errores humanos mas que nada
                if(permiso.equals(Manifest.permission.CALL_PHONE)){
                    if(valorPermiso == PackageManager.PERMISSION_GRANTED){
                        Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numeroTelefono));
                        startActivity(intentLlamada);
                    }
                    else{
                        Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case CAMERA_CODE:
                int valor = grantResults[0];
                if(valor == PackageManager.PERMISSION_GRANTED){
                    Intent intentCamara = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intentCamara, CAMERA_CODE);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void activarCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                if(resultCode == RESULT_OK){
                    //Para recibir imagenes gerneralmente se trabaja en formato mapa de bits
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    ivImagen.setImageBitmap(foto);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}