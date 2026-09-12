package com.example.ekran_startowy

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

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

        // INICJALIZACJA WIDOKÓW
        val editTextNazwa = findViewById<EditText>(R.id.nazwa_aktywnosci)
        val editTextCzas = findViewById<EditText>(R.id.czas_trwania)
        val spinnerKategorie = findViewById<Spinner>(R.id.wybierz_kategorie)
        val spinnerPriorytety = findViewById<Spinner>(R.id.priorytet)
        val buttonZapisz = findViewById<Button>(R.id.Zapisz)
        val buttonAnuluj = findViewById<Button>(R.id.anuluj)

        // PODPIĘCIE DANYCH DO SPINNERÓW
        val kategorie = listOf("Sport", "Nauka", "Rozrywka", "Turystyka", "Spotkanie", "Inne")
        val priorytety = listOf("Niski", "Średni", "Wysoki")

        val adapterKategorie = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, kategorie)
        val adapterPriorytety = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorytety)

        spinnerKategorie.adapter = adapterKategorie
        spinnerPriorytety.adapter = adapterPriorytety

        // OBSŁUGA PRZYCISKU "ANULUJ"
        buttonAnuluj.setOnClickListener {
            finish()
        }

        // OBSŁUGA PRZYCISKU "ZAPISZ"
        buttonZapisz.setOnClickListener {
            val nazwaAktywnosci = editTextNazwa.text.toString().trim()
            val kategoria = spinnerKategorie.selectedItem.toString()
            val czasAktywnosci = editTextCzas.text.toString().trim().toIntOrNull()
            val priorytet = spinnerPriorytety.selectedItem.toString()

            val errorInfoText = findViewById<TextView>(R.id.errorInfo)

            if (nazwaAktywnosci.isEmpty()) {
                errorInfoText.text = "UZUPEŁNIJ NAZWĘ!"
                errorInfoText.visibility = View.VISIBLE
            } else if (czasAktywnosci == null) {
                errorInfoText.text = "UZUPEŁNIJ CZAS POPRAWNIE!"
                errorInfoText.visibility = View.VISIBLE
            } else if (czasAktywnosci <= 0) {
                errorInfoText.text = "CZAS MUSI BYĆ DODATNI!"
                errorInfoText.visibility = View.VISIBLE
            } else {
                errorInfoText.visibility = View.INVISIBLE

                // FUNKCJA DO WYKONANIA ZAPISU
                fun wykonajZapis() {
                    val plik = File(filesDir, "aktywnosci.json")

                    val tablicaJSON = if (plik.exists() && plik.readText().isNotEmpty()) {
                        JSONArray(plik.readText())
                    } else {
                        JSONArray()
                    }

                    var maxId = 0
                    for (i in 0 until tablicaJSON.length()) {
                        val obiekt = tablicaJSON.getJSONObject(i)
                        val id = obiekt.optInt("id", 0)
                        if (id > maxId) {
                            maxId = id
                        }
                    }
                    val noweId = maxId + 1

                    val nowaAktywnosc = JSONObject().apply {
                        put("id", noweId)
                        put("nazwa", nazwaAktywnosci)
                        put("kategoria", kategoria)
                        put("czas", czasAktywnosci)
                        put("priorytet", priorytet)
                    }

                    tablicaJSON.put(nowaAktywnosc)
                    plik.writeText(tablicaJSON.toString(4))

                    AlertDialog.Builder(this)
                        .setTitle("Sukces")
                        .setMessage("Pomyślnie zapisano nową aktywność")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }
                        .setCancelable(false)
                        .show()
                }

                // SPRAWDZENIE CZY CZAS JEST WIĘKSZY NIŻ 180 MINUT
                if (czasAktywnosci > 180) {
                    AlertDialog.Builder(this)
                        .setTitle("Uwaga!")
                        .setMessage("Aktywność jest długa: $czasAktywnosci min! Na pewno chcesz ją zapisać?")
                        .setPositiveButton("TAK") { _, _ ->
                            wykonajZapis()
                        }
                        .setNegativeButton("NIE", null)
                        .setCancelable(false)
                        .show()
                } else {
                    wykonajZapis()
                }
            }
        }
    }
}