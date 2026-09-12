package com.example.ekran_startowy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import java.io.File

class SczegolyAktywnosci : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sczegoly_aktywnosci)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. ODBIÓR DANYCH Z INTENTU
        val id = intent.getIntExtra("ID", -1)
        val nazwa = intent.getStringExtra("NAZWA") ?: ""
        val kategoria = intent.getStringExtra("KATEGORIA") ?: ""
        val czas = intent.getIntExtra("CZAS", 0)
        val priorytet = intent.getStringExtra("PRIORYTET") ?: ""

        // 2. PRZYPISANIE DO WIDOKÓW
        val textNazwa = findViewById<TextView>(R.id.textNazwa)
        val textKategoria = findViewById<TextView>(R.id.textKategoria)
        val textCzas = findViewById<TextView>(R.id.textCzas)
        val textPriorytet = findViewById<TextView>(R.id.textPriorytet)

        textNazwa.text = nazwa
        textKategoria.text = kategoria
        textCzas.text = "$czas minut"
        textPriorytet.text = priorytet

        // 3. OBSŁUGA PRZYCISKU WRÓĆ
        val buttonWroc = findViewById<Button>(R.id.buttonWroc)
        buttonWroc.setOnClickListener {
            finish()
        }

        // 4. OBSŁUGA PRZYCISKU USUŃ
        val buttonUsun = findViewById<Button>(R.id.buttonUsun)
        buttonUsun.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Usuwanie")
                .setMessage("Czy na pewno chcesz usunąć tę aktywność?")
                .setPositiveButton("Tak") { _, _ ->
                    usunPoId(id)
                }
                .setNegativeButton("Nie", null)
                .show()
        }
    }

    private fun usunPoId(idDoUsuniecia: Int) {
        val plik = File(filesDir, "aktywnosci.json")
        if (!plik.exists() || plik.readText().isEmpty()) return

        val tablicaJSON = JSONArray(plik.readText())
        val nowaTablicaJSON = JSONArray()

        for (i in 0 until tablicaJSON.length()) {
            val obiekt = tablicaJSON.getJSONObject(i)
            if (obiekt.getInt("id") != idDoUsuniecia) {
                nowaTablicaJSON.put(obiekt)
            }
        }
        for (i in 0 until nowaTablicaJSON.length()) {
            val obiekt = nowaTablicaJSON.getJSONObject(i)
            obiekt.put("id", i + 1)
        }
        plik.writeText(nowaTablicaJSON.toString(4))
        finish()
    }
}