package us.cyosp.codewonderland.champlaintrivia.controller

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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
    private var mHint: Button? = null
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

        // grab the parts of our layout we need to modify
        mPrompt = findViewById(R.id.question)
        mAnswers.add(findViewById(R.id.answer0))
        mAnswers.add(findViewById(R.id.answer1))
        mAnswers.add(findViewById(R.id.answer2))
        mAnswers.add(findViewById(R.id.answer3))
        mNext = findViewById(R.id.nextButton)
        mHint = findViewById(R.id.hintButton)

        // set up quiz
        val quizIndex = this.intent!!.extras!!.getInt(QuizActivity.Intent.EXTRA_QUIZ_INDEX)

        mQuiz = QuizSelection.Data.Quizzes[quizIndex]
        mQuestion = mQuiz!!.getQuestion()

        setQuestion()

        // add functionality to next button
        setNextListener()
    }

    private fun setQuestion() {
        mPrompt!!.text = mQuestion!!.getPrompt()

        val answers = mQuestion!!.getAnswers()

        // update answer buttons with current options
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

        // add event listeners to answer buttons
        for (answer in mAnswers) {
            setAnswerListener(answer)
        }

        // determine a random wrong answer to remove for hint
        var wrongAnswer: Button? = null

        for (answer in mAnswers.shuffled()) {

            if (!mQuestion!!.checkAnswer(answer.text.toString())) {
                wrongAnswer = answer
                break
            }
        }

        // remove incorrect option and make hint button disappear
        mHint!!.setOnClickListener {
            wrongAnswer?.visibility = View.GONE
            mHint!!.visibility = View.GONE

            mHint!!.setOnClickListener(null)
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

            val result = mQuiz!!.submitAnswer(answer.text.toString())

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
            // TODO: Add sound response to answer

            if (mQuiz!!.onLastQuestion()) {
                mNext!!.setText(R.string.submit_button)
            }

            mHint!!.visibility = View.GONE
            mNext!!.visibility = View.VISIBLE
        }
    }

    private fun setNextListener() {
        mNext!!.setOnClickListener {
            mNext!!.visibility = View.GONE

            if (mQuiz!!.onLastQuestion()) {
                // TODO: Submit Quiz
                Log.d("QuizActivity", "in conditional")
            } else {
                mQuestion = mQuiz!!.nextQuestion()

                for (answer in mAnswers) {
                    answer.setBackgroundColor(Color.parseColor("#236192"))
                    answer.visibility = View.VISIBLE
                }
                setQuestion()
            }
        }
    }

    private fun clearListener(button: Button) {
        button.setOnClickListener(null)
    }
}