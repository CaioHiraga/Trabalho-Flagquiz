package com.example.flagquiztrabalho

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Pais(val nome: String, val imagemId : Int)
class QuizActivity : AppCompatActivity() {

    val listaDePaises = listOf(
        Pais("Brasil", R.drawable.flag_brazil),
        Pais("Argentina", R.drawable.flag_argentina),
        Pais("Barbados", R.drawable.flag_barbados),
        Pais("Angola", R.drawable.flag_angola),
        Pais("China", R.drawable.flag_china),
        Pais("Cuba", R.drawable.flag_cuba),
        Pais("Egito", R.drawable.flag_egito),
        Pais("Gana", R.drawable.flag_ghana),
        Pais("Itália", R.drawable.flag_italia),
        Pais("Jamaica", R.drawable.flag_jamaica),
        Pais("Marrocos", R.drawable.flag_marrocos),
        Pais("Rússia", R.drawable.flag_russia),
        Pais("Suécia", R.drawable.flag_suecia),
        Pais("Alemanha", R.drawable.flag_germany),
        Pais("França", R.drawable.flag_france),
        Pais("Japão", R.drawable.flag_japan)
    )

    lateinit var paisesRodada: List<Pais>
    var indiceAtual = 0
    var pontos = 0
    val resumoRodada = mutableListOf<String>()


    lateinit var textViewContador: TextView
    lateinit var imgFlag: ImageView
    lateinit var editTextResposta: EditText
    lateinit var buttonVerificar: Button
    lateinit var buttonProxima: Button

    lateinit var textViewFeedback: TextView


    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)

        user = IntentCompat.getParcelableExtra(intent, "user", User::class.java) ?: run {
            Toast.makeText(
                this,
                "Erro ao carregar dados do jogador",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewContador = findViewById(R.id.textViewContador)
        imgFlag = findViewById(R.id.imgFlag)
        editTextResposta = findViewById(R.id.editTextResposta)
        textViewFeedback = findViewById(R.id.textViewFeedback)
        buttonVerificar = findViewById(R.id.buttonVerificar)
        buttonProxima = findViewById(R.id.buttonProxima)

        buttonVerificar.setOnClickListener {
            verificarResposta()
        }

        buttonProxima.setOnClickListener {
            proximaPergunta()
        }

        novaRodada()
    }

    fun novaRodada() {
        paisesRodada = listaDePaises.shuffled().take(5)
        indiceAtual = 0
        pontos = 0
        resumoRodada.clear()
        exibirPerguntaAtual()
    }

    fun exibirPerguntaAtual() {
        val paisAtual = paisesRodada[indiceAtual]

        textViewContador.text = "Pergunta ${indiceAtual + 1} de ${paisesRodada.size}"
        imgFlag.setImageResource(paisAtual.imagemId)
        editTextResposta.text.clear()
        textViewFeedback.text = ""
        textViewFeedback.visibility = View.GONE


        // Controla a visibilidade e estado dos botões
        buttonVerificar.isEnabled = true
        editTextResposta.isEnabled = true
        buttonProxima.visibility = View.GONE
    }

    fun verificarResposta() {
        val resposta = editTextResposta.text.toString().trim()

        if (resposta.isEmpty()) {
            editTextResposta.error = "Digite uma resposta"
            return
        }

        val paisCorreto = paisesRodada[indiceAtual]
        val numeroPergunta = indiceAtual + 1

        if (resposta.equals(paisCorreto.nome, ignoreCase = true)) {
            pontos += 20

            textViewFeedback.text = "Correto!"
            textViewFeedback.setTextColor(android.graphics.Color.rgb(0, 128, 0))

            resumoRodada.add(
                "$numeroPergunta: Correto! ${paisCorreto.nome}"
            )
        } else {
            textViewFeedback.text =
                "Incorreto! Resposta correta: ${paisCorreto.nome}"

            textViewFeedback.setTextColor(
                android.graphics.Color.rgb(190, 0, 0)
            )

            resumoRodada.add(
                "$numeroPergunta: Incorreto — resposta correta: ${paisCorreto.nome}"
            )
        }

        textViewFeedback.visibility = View.VISIBLE
        buttonVerificar.isEnabled = false
        editTextResposta.isEnabled = false
        buttonProxima.visibility = View.VISIBLE
    }


    fun proximaPergunta() {
        indiceAtual++

        if (indiceAtual < paisesRodada.size) {
            exibirPerguntaAtual()
        } else {
            finalizarJogo()
        }
    }

    fun finalizarJogo() {

        user.pontos = this.pontos

        val intent = Intent(this, ResultActivity::class.java)

        //intents para passar os parametros
        intent.putExtra("PLAYER_NAME", user.nome)
        intent.putExtra("FINAL_SCORE", user.pontos)
        intent.putExtra("RESULTS_DETAILS", ArrayList(resumoRodada))


        startActivity(intent)
        finish()

    }
}