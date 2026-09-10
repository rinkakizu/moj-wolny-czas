package com.example.ekran_startowy

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DodawanieAktywnosci : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dodawanie_aktywnosci)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 1. INICJALIZACJA WIDOKÓW
        val editTextNazwa = findViewById<EditText>(R.id.nazwa_aktywnosci)
        val editTextCzas = findViewById<EditText>(R.id.czas_trwania)
        val spinnerKategorie = findViewById<Spinner>(R.id.wybierz_kategorie)
        val spinnerPriorytety = findViewById<Spinner>(R.id.priorytet)
        val buttonZapisz = findViewById<Button>(R.id.Zapisz)
        val buttonAnuluj = findViewById<Button>(R.id.anuluj)

        // 2. PODPIĘCIE DATA DO SPINNERÓW
        //listy opcji do wyboru
        val kategorie = listOf("Sport", "Nauka", "Rozrywka", "Turystyka", "Spotkanie", "Inne")
        val priorytety = listOf("Niski", "Średni", "Wysoki")
        //utworzenie adapterów rozwijanej listy
        val adapterKategorie = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kategorie)
        val adapterPriorytety = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorytety)
        //przypisanie adapterów do odpowiednich widoków
        spinnerKategorie.adapter = adapterKategorie
        spinnerPriorytety.adapter = adapterPriorytety

        //OBSŁUGA PRZYCISKU "ANULUJ"
        buttonAnuluj.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}