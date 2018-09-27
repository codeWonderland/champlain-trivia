package us.cyosp.codewonderland.champlaintrivia.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.controller.QuizManager

class QuizSelection : AppCompatActivity() {

    private var mQuizManager: QuizManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_selection)

        val dataStream = assets.open("quiz_data.xml")
        this.mQuizManager = QuizManager(dataStream) {
            quizzes: List<String> -> displayQuizzes(quizzes)
        }
    }

    private fun displayQuizzes(quizzes: List<String>) {
        return Unit
    }
}