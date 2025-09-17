package com.example.flashcard_appnew

import android.graphics.Color
import android.os.Bundle
import android.view.View // ✅ AJOUTÉ pour pouvoir utiliser View.GONE / View.VISIBLE
import android.widget.ImageView // ✅ AJOUTÉ pour gérer l’icône
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Liste des questions et réponses (la bonne réponse est toujours en première position)
    private val questions = listOf(
        "Quelle est la capitale d'Haïti ?" to listOf("Port-au-Prince", "Cap-Haïtien", "Jacmel"),
        "Qui est le père fondateur d'Haïti ?" to listOf("Jean-Jacques Dessalines", "Henri Christophe", "Toussaint Louverture"),
        "En quelle année Haïti a proclamé son indépendance ?" to listOf("1804", "1492", "1825"),
        "Quel est le plat national d'Haïti ?" to listOf("Soup Joumou 🥣", "Riz collé", "Griot"),
        "Quelle est la devise d'Haïti ?" to listOf("Liberté – Égalité – Fraternité", "Union fait la force", "Indépendance ou la mort")
    )

    private var currentIndex = 0 // index de la question actuelle
    private var isShowingAnswers = true // ✅ AJOUTÉ : état pour savoir si les réponses sont visibles ou non

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val answer1 = findViewById<TextView>(R.id.answer1)
        val answer2 = findViewById<TextView>(R.id.answer2)
        val answer3 = findViewById<TextView>(R.id.answer3)

        // ✅ AJOUTÉ : récupérer l’icône "œil"
        val toggleButton = findViewById<ImageView>(R.id.toggle_choices_visibility)

        // Liste pour gérer facilement les réponses
        val answersViews = listOf(answer1, answer2, answer3)

        fun loadQuestion() {
            val (question, answers) = questions[currentIndex]
            flashcardQuestion.text = question

            // Mélanger l’ordre des réponses
            val shuffledAnswers = answers.shuffled()

            // Assigner les réponses
            for (i in answersViews.indices) {
                answersViews[i].text = shuffledAnswers[i]
                answersViews[i].setBackgroundResource(R.drawable.background_answer)
                answersViews[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                answersViews[i].isClickable = true
                answersViews[i].visibility = if (isShowingAnswers) View.VISIBLE else View.GONE // ✅ AJOUTÉ pour respecter l’état actuel
            }

            // Identifier la bonne réponse
            val correctAnswer = answers[0]

            // Gérer les clics
            for (answerView in answersViews) {
                answerView.setOnClickListener {
                    if (answerView.text == correctAnswer) {
                        answerView.setBackgroundColor(Color.GREEN) // Bonne réponse
                    } else {
                        answerView.setBackgroundColor(Color.RED)   // Mauvaise réponse
                        // Montrer la bonne en vert
                        answersViews.first { it.text == correctAnswer }
                            .setBackgroundColor(Color.GREEN)
                    }

                    // Empêcher de recliquer
                    answersViews.forEach { it.isClickable = false }

                    // Passer à la prochaine question après 1.5 sec
                    answerView.postDelayed({
                        currentIndex = (currentIndex + 1) % questions.size
                        loadQuestion()
                    }, 1500)
                }
            }
        }

        // ✅ AJOUTÉ : logique du bouton œil
        toggleButton.setOnClickListener {
            if (isShowingAnswers) {
                // Cacher les réponses
                answersViews.forEach { it.visibility = View.GONE }
                toggleButton.setImageResource(R.drawable.hide_icon) // changer icône
            } else {
                // Montrer les réponses
                answersViews.forEach { it.visibility = View.VISIBLE }
                toggleButton.setImageResource(R.drawable.show_icon)
            }
            isShowingAnswers = !isShowingAnswers
        }

        // Charger la 1ère question
        loadQuestion()
    }
}
