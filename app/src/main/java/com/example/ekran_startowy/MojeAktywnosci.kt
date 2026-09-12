package com.example.ekran_startowy

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import java.io.File

class MojeAktywnosci : AppCompatActivity() {

    private lateinit var listView: ListView
    private val listaObiektow = ArrayList<Aktywnosc>()
    private val listaTekstow = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_moje_aktywnosci)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.listaAktywnosci)

        // OBSŁUGA PRZYCISKU "WRÓĆ"
        val buttonWroc = findViewById<Button>(R.id.wroc)
        buttonWroc.setOnClickListener {
            finish()
        }

        // FILTROWANIE KATEGORII
        val spinnerKategorie = findViewById<Spinner>(R.id.wyborKategorii)
        val kategorie = listOf("Wszystkie", "Sport", "Nauka", "Rozrywka", "Turystyka", "Spotkanie", "Inne")
        val adapterKategorie = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kategorie)
        spinnerKategorie.adapter = adapterKategorie

        // KLIKNIĘCIE W ELEMENT LISTY -> PRZEJŚCIE DO SZCZEGÓŁÓW
        listView.setOnItemClickListener { _, _, position, _ ->
            val wybrane = listaObiektow[position]

            val intent = Intent(this, SczegolyAktywnosci::class.java).apply {
                putExtra("ID", wybrane.id)
                putExtra("NAZWA", wybrane.nazwa)
                putExtra("KATEGORIA", wybrane.kategoria)
                putExtra("CZAS", wybrane.czas)
                putExtra("PRIORYTET", wybrane.priorytet)
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        wczytajDane()
    }

    //FUNKCJA KTÓRA WCZYTUJE DANE Z PLIKU JSON
    private fun wczytajDane() {
        listaObiektow.clear()
        listaTekstow.clear()

        val plik = File(filesDir, "aktywnosci.json")

        if (plik.exists() && plik.readText().isNotEmpty()) {
            val tablicaJSON = JSONArray(plik.readText())

            for (i in 0 until tablicaJSON.length()) {
                val obiekt = tablicaJSON.getJSONObject(i)

                val aktywnosc = Aktywnosc(
                    id = obiekt.getInt("id"),
                    nazwa = obiekt.getString("nazwa"),
                    kategoria = obiekt.getString("kategoria"),
                    czas = obiekt.getInt("czas"),
                    priorytet = obiekt.getString("priorytet")
                )

                listaObiektow.add(aktywnosc)
                listaTekstow.add("${aktywnosc.id}. ${aktywnosc.nazwa} (${aktywnosc.kategoria}) - ${aktywnosc.czas} min | ${aktywnosc.priorytet}")
            }
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaTekstow
        )
        listView.adapter = adapter
    }
}