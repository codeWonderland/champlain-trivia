package us.cyosp.codewonderland.champlaintrivia.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.model.Score

class QuizResults : AppCompatActivity() {

    private val REQUEST_CODE_RECORDS = 1

    private var mScore: Int = 0
    private var mScoreView: TextView? = null
    private var mUsername: EditText? = null
    private var mSubmit: Button? = null

    companion object {
        const val EXTRA_QUIZ_RESULTS: String =
                "us.cyosp.codewonderland.champlaintrivia.quiz_results"

        fun newIntent(packageContext: Context, quizResults: Int): android.content.Intent {
            val intent = Intent(packageContext, QuizResults::class.java)
            intent.putExtra(EXTRA_QUIZ_RESULTS, quizResults)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_results)

        mScore = this.intent!!.extras!!.getInt(QuizResults.EXTRA_QUIZ_RESULTS)

        mScoreView = findViewById(R.id.score)
        mUsername = findViewById(R.id.username)
        mSubmit = findViewById(R.id.submit_username)

        mScoreView!!.text = mScore.toString()

        mSubmit!!.setOnClickListener {
            val username = mUsername!!.text.toString()

            if (username != "") {
                val intent: android.content.Intent =
                        QuizRecords.newIntent(this, Score(username, mScore))

                startActivityForResult(intent, REQUEST_CODE_RECORDS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, intent: android.content.Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == this.REQUEST_CODE_RECORDS) {
                setResult(Activity.RESULT_OK)
                this.finish()
        }
    }
}