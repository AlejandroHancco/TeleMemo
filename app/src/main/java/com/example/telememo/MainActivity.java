package com.example.telememo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnSoftware, btnCiber, btnOpticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura la barra de herramientas (Toolbar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cambia el texto del título en la barra de herramientas
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("APPSIoT - Lab 2");

        // Encuentra los botones en la pantalla
        btnSoftware = findViewById(R.id.btnSoftware);
        btnCiber = findViewById(R.id.btnCiberseguridad);
        btnOpticas = findViewById(R.id.btnOpticas);

        // Configura los botones para que al hacer clic se inicie un nuevo juego con diferentes temáticas
        btnSoftware.setOnClickListener(v -> startGame("Software"));
        btnCiber.setOnClickListener(v -> startGame("Ciberseguridad"));
        btnOpticas.setOnClickListener(v -> startGame("Ópticas"));
    }

    // Esta función abre una nueva pantalla para jugar con la temática seleccionada
    private void startGame(String tematica) {
        Intent intent = new Intent(MainActivity.this, TeleMemoActivity.class);
        intent.putExtra("tematica", tematica); // Pasa la temática seleccionada a la siguiente pantalla
        startActivity(intent); // Abre la nueva pantalla
    }
}
