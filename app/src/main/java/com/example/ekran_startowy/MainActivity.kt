package com.example.ekran_startowy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //DODANIE FUNKCJONALNOŚĆI PRZYCISKU "DODAJ AKTYWNOŚĆ"
        val buttonDodajAktywnosc = findViewById<Button>(R.id.dodaj_aktywnosc)
        buttonDodajAktywnosc.setOnClickListener {
            val intent = Intent(this, DodawanieAktywnosci::class.java)
            startActivity(intent)
        }

        //DODANIE FUNKCJONALNOŚĆI PRZYCISKU "MOJE AKTYWNOŚCI"
        val buttonMojeAktywnosci = findViewById<Button>(R.id.moje_aktywnosci)
        buttonMojeAktywnosci.setOnClickListener {
            val intent = Intent(this, MojeAktywnosci::class.java)
            startActivity(intent)
        }
    }
}