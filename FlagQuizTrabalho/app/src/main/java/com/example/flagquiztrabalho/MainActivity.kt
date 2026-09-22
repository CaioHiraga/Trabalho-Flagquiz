package com.example.flagquiztrabalho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun iniciarJogo(view: View){
        val jogador1 = findViewById<EditText>(R.id.Jogador1).text.toString()
        if (jogador1.isEmpty()){
            Toast.makeText(this, "Por favor insira um nome", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, QuizActivity::class.java)
        val user= User(nome=jogador1, pontos=0)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}

