package us.cyosp.codewonderland.champlaintrivia.controller

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.model.Question
import us.cyosp.codewonderland.champlaintrivia.model.Quiz
import java.lang.Exception

class QuizActivity : AppCompatActivity() {

    private var mQuiz: Quiz? = null
    private var mPrompt: TextView? = null
    private var mAnswers = ArrayList<Button>()
    private var mNext: Button? = null
    private var mQuestion: Question? = null

    object Intent {
        const val EXTRA_QUIZ_INDEX: String =
                "us.cyosp.codewonderland.champlaintrivia.quiz_data"

        fun newIntent(packageContext: Context, quizIndex: Int): android.content.Intent {
            val intent = Intent(packageContext, QuizActivity::class.java)
            intent.putExtra(EXTRA_QUIZ_INDEX, quizIndex)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        mPrompt = findViewById(R.id.question)
        mAnswers.add(findViewById(R.id.answer0))
        mAnswers.add(findViewById(R.id.answer1))
        mAnswers.add(findViewById(R.id.answer2))
        mAnswers.add(findViewById(R.id.answer3))
        mNext = findViewById(R.id.nextButton)

        val quizIndex = this.intent!!.extras!!.getInt(QuizActivity.Intent.EXTRA_QUIZ_INDEX)

        mQuiz = QuizSelection.Data.Quizzes[quizIndex]
        mQuestion = mQuiz!!.getQuestion()

        setQuestion()

        for (answer in mAnswers) {
            setAnswerListener(answer)
        }
    }

    private fun setQuestion() {
        mPrompt!!.text = mQuestion!!.getPrompt()

        val answers = mQuestion!!.getAnswers()

        for (i in answers.indices) {
            if (mQuestion!!.mType == "text") {
                mAnswers[i].text = answers[i]
            } else {
                try {  // TODO: Test this
                    mAnswers[i].setBackgroundResource(
                            resources.getIdentifier(answers[i], "String", packageName)
                    )
                } catch (e: Exception) {

                }
            }
        }
    }

    private fun setAnswerListener(answer: Button) {
        answer.setOnClickListener {
            for (button in mAnswers) {
                if (button.text != answer.text) {
                    button.setBackgroundColor(Color.GRAY)
                }

                clearListener(button)
            }

            val result = mQuestion!!.checkAnswer(answer.text.toString())

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
            /*
            TODO: Create answer system

            This should go as follows:
            - grey out other answers (done)
            - check answer (currently the answer is always false)
            - display feedback via toast
            - display next question button
             */
        }
    }

    private fun clearListener(button: Button) {
        button.setOnClickListener(null)
    }

    /*
    function adopted from:
        https://stackoverflow.com/questions/36826004/kotlin-for-android-toast
    */
    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}