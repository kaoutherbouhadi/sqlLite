package com.example.sqlite



import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EtudiantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = EtudiantDBHelper(applicationContext)
        val db = dbHelper.readableDatabase
        val etudiantsList = getAllEtudiants(db)

        recyclerView = findViewById(R.id.recyclerViewEtudiants)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = EtudiantAdapter(etudiantsList)
        recyclerView.adapter = adapter

        val btnAjouter: Button = findViewById(R.id.btnAjouter)
        btnAjouter.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllEtudiants(db: SQLiteDatabase): List<EtudiantBC> {
        val etudiantsList = mutableListOf<EtudiantBC>()

        val projection = arrayOf(
            EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_GENDER,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP
        )

        val cursor = db.query(
            EtudiantBC.EtudiantEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val nom = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM))
                val prenom = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM))
                val phone = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE))
                val gender = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_GENDER))
                val email = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL))
                val mdp = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP))
                val etudiant = EtudiantBC(nom, prenom, phone, gender, email, mdp)
                etudiantsList.add(etudiant)
            }
        }

        cursor.close()
        return etudiantsList
    }
}
