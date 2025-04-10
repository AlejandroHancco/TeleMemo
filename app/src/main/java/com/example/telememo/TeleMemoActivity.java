package com.example.telememo;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TeleMemoActivity extends AppCompatActivity {

    private String tematica;
    private TextView textTematica;
    private GridLayout gridPalabras;
    private TextView textIntentos;
    private int intentos = 0;
    private int indicePalabra = 0;
    private List<String> oracion;
    private List<Button> botonesPalabra = new ArrayList<>();
    private long inicioTiempo;
    private Button btnNuevoJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telememo);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón atrás en Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Obtener vistas
        textTematica = findViewById(R.id.textTematica);
        gridPalabras = findViewById(R.id.gridPalabras);
        textIntentos = findViewById(R.id.textIntentos);
        btnNuevoJuego = findViewById(R.id.btnNuevoJuego);
        ImageButton btnHistorial = findViewById(R.id.btnHistorial);

        // Acciones del botón "Nuevo Juego"
        btnNuevoJuego.setOnClickListener(v -> {
            Intent intent = new Intent(TeleMemoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Acción del botón "Historial"
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(TeleMemoActivity.this, HistorialActivity.class);
            startActivity(intent);
        });

        // Recibir temática desde MainActivity
        tematica = getIntent().getStringExtra("tematica");
        textTematica.setText("Temática seleccionada: " + tematica);

        oracion = obtenerOracionPorTematica(tematica);
        List<String> palabrasMezcladas = new ArrayList<>(oracion);
        Collections.shuffle(palabrasMezcladas);

        mostrarPalabrasEnGrid(palabrasMezcladas);
        inicioTiempo = SystemClock.elapsedRealtime();
    }

    // Botón de volver
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<String> obtenerOracionPorTematica(String tematica) {
        Random random = new Random();
        switch (tematica) {
            case "Software":
                List<String> softwareOraciones = Arrays.asList(
                        "Los fragments reutilizan partes de pantalla en distintas actividades de la app",
                        "Los intents permiten acceder a apps como la cámara o WhatsApp directamente"
                );
                return Arrays.asList(softwareOraciones.get(random.nextInt(softwareOraciones.size())).split(" "));

            case "Ciberseguridad":
                List<String> ciberseguridadOraciones = Arrays.asList(
                        "Una VPN encripta tu conexión para navegar de forma anónima y segura",
                        "El ataque DDoS satura servidores con tráfico falso y causa caídas masivas"
                );
                return Arrays.asList(ciberseguridadOraciones.get(random.nextInt(ciberseguridadOraciones.size())).split(" "));

            case "Ópticas":
                List<String> opticasOraciones = Arrays.asList(
                        "La fibra óptica envía datos a gran velocidad evitando cualquier interferencia eléctrica",
                        "Los amplificadores EDFA mejoran la señal óptica en redes de larga distancia"
                );
                return Arrays.asList(opticasOraciones.get(random.nextInt(opticasOraciones.size())).split(" "));

            default:
                return new ArrayList<>();
        }
    }

    private void mostrarPalabrasEnGrid(List<String> palabras) {
        gridPalabras.removeAllViews();
        botonesPalabra.clear();

        for (String palabra : palabras) {
            Button btnPalabra = new Button(this);
            btnPalabra.setText("???");
            btnPalabra.setTag(palabra);
            btnPalabra.setOnClickListener(this::manejarSeleccion);

            gridPalabras.addView(btnPalabra);
            botonesPalabra.add(btnPalabra);
        }
    }

    private void manejarSeleccion(View v) {
        Button btn = (Button) v;
        String palabraSeleccionada = (String) btn.getTag();

        if (oracion.contains(palabraSeleccionada)) {
            if (indicePalabra == 0) {
                btn.setText(palabraSeleccionada);
                btn.setEnabled(false);
                indicePalabra++;
            } else {
                int indiceSeleccionado = oracion.indexOf(palabraSeleccionada);
                if (indiceSeleccionado == (oracion.indexOf(oracion.get(indicePalabra)) + 1)) {
                    btn.setText(palabraSeleccionada);
                    btn.setEnabled(false);
                    indicePalabra++;
                } else {
                    fallo();
                }
            }

            if (indicePalabra == oracion.size()) {
                long tiempoTotal = (SystemClock.elapsedRealtime() - inicioTiempo) / 1000;
                Toast.makeText(this, "¡Ganaste! Tiempo: " + tiempoTotal + "s", Toast.LENGTH_LONG).show();
                guardarResultado("Ganado");
            }
        } else {
            fallo();
        }
    }

    private void fallo() {
        intentos++;
        textIntentos.setText("Intentos: " + intentos);
        Toast.makeText(this, "Incorrecto. Intentos restantes: " + (3 - intentos), Toast.LENGTH_SHORT).show();

        for (Button b : botonesPalabra) {
            b.setText("???");
            b.setEnabled(true);
        }

        indicePalabra = 0;

        if (intentos >= 3) {
            Toast.makeText(this, "Perdiste :(", Toast.LENGTH_LONG).show();
            guardarResultado("Perdido");

            for (Button b : botonesPalabra) {
                b.setText("???");
                b.setEnabled(false);
            }
        }
    }

    private void guardarResultado(String resultado) {
        // Guardar el resultado en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("Historial", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Obtenemos el número de juegos guardados
        int numJuegos = preferences.getInt("numJuegos", 0);
        numJuegos++;

        // Guardamos el resultado del juego actual
        editor.putInt("numJuegos", numJuegos);
        editor.putString("juego_" + numJuegos, resultado);

        // Aplicar cambios
        editor.apply();
    }
}
