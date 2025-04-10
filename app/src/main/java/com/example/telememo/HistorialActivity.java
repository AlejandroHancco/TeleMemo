package com.example.telememo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HistorialActivity extends AppCompatActivity {

    private Button btnNuevoJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Establece la barra de herramientas (Toolbar) como parte de la interfaz
        Toolbar toolbar = findViewById(R.id.toolbarHistorial);
        setSupportActionBar(toolbar);

        // Muestra el botón de retroceso en la barra de herramientas
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Establece el título de la barra de herramientas
        getSupportActionBar().setTitle("Historial");

        // Configura el botón "Nuevo Juego"
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego);
        btnNuevoJuego.setOnClickListener(v -> {
            // Al hacer clic en el botón "Nuevo Juego", volver a MainActivity
            Intent intent = new Intent(HistorialActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Opcional: Finalizar esta actividad para evitar que el usuario vuelva a ella con el botón de "Atrás"
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Al presionar el botón de retroceso, vuelve a la pantalla anterior
        onBackPressed();
        return true;
    }
}
