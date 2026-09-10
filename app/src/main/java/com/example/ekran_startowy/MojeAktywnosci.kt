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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_moje_aktywnosci)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //OBSŁUGA PRZYCISKU "WRÓĆ"
        val buttonWroc = findViewById<Button>(R.id.wroc)
        buttonWroc.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //FILTROWANIE KATEGORII
        val spinnerKategorie = findViewById<Spinner>(R.id.wyborKategorii)
        val kategorie = listOf("Wszystkie", "Sport", "Nauka", "Rozrywka", "Turystyka", "Spotkanie", "Inne")
        val adapterKategorie = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kategorie)
        spinnerKategorie.adapter = adapterKategorie

        //ODCZYTYWANIE DANYCH Z PLIKU JSON
        val lista = findViewById<ListView>(R.id.listaAktywnosci)
        val listaTekstow = ArrayList<String>()

        val plik = File(filesDir, "aktywnosci.json")

        if (plik.exists() && plik.readText().isNotEmpty()) {
            val tablicaJSON = JSONArray(plik.readText())

            // ODCZYT KAZDEGO OBIEKTU Z PLIKU
            for (i in 0 until tablicaJSON.length()) {
                val obiekt = tablicaJSON.getJSONObject(i)

                val id = obiekt.getInt("id")
                val nazwa = obiekt.getString("nazwa")
                val kategoria = obiekt.getString("kategoria")
                val czas = obiekt.getInt("czas")
                val priorytet = obiekt.getString("priorytet")

                // LACZENIE DANYCH W JEDEN NAPIS
                listaTekstow.add("$id. $nazwa ($kategoria) - $czas min | $priorytet")
            }
        }

        // LISTA NAPISOW JEST WRZUCANA DO LISTVIEW
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaTekstow
        )
        lista.adapter = adapter
    }
}