package us.cyosp.codewonderland.champlaintrivia.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import us.cyosp.codewonderland.champlaintrivia.R

class QuizResults : AppCompatActivity() {

    object Intent {
        const val EXTRA_QUIZ_RESULTS: String =
                "us.cyosp.codewonderland.champlaintrivia.quiz_results"

        fun newIntent(packageContext: Context, quizResults: Int): android.content.Intent {
            val intent = Intent(packageContext, QuizResults::class.java)
            intent.putExtra(EXTRA_QUIZ_RESULTS, quizResults)
            return intent
        }
    }

    private var mScore: Int = 0
    private var mScoreView: TextView? = null
    private var mResultsPrompt: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_results)

        mScore = this.intent!!.extras!!.getInt(QuizResults.Intent.EXTRA_QUIZ_RESULTS)

        mScoreView = findViewById(R.id.score)
        mResultsPrompt = findViewById(R.id.results_prompt)

        mScoreView!!.text = mScore.toString()


    }
}